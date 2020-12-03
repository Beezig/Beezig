package eu.beezig.core.util.obs;

import com.google.crypto.tink.subtle.Ed25519Sign;
import com.google.crypto.tink.subtle.X25519;
import eu.beezig.core.Beezig;
import eu.beezig.core.config.Settings;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.task.WorldTask;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.TextButton;
import eu.the5zig.mod.util.component.MessageComponent;
import eu.the5zig.mod.util.component.style.MessageAction;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ObsState {
    private static final UUID APP_UUID = UUID.fromString("3552936e-4224-48d0-b0e8-b0f08be197f9");
    private static final JSONParser PARSER = new JSONParser();

    private byte[] x25519Private;
    private Ed25519Sign messageSigner;
    private boolean startedRecording;
    private ScheduledFuture<?> detectTask;

    public ObsState() {
        detectTask = Beezig.get().getAsyncExecutor().scheduleAtFixedRate(() -> {
            if(!ServerHive.isCurrent()) return;
            try {
                int status = ObsHttp.sendGet("/").getCode();
                if(status != 200) return;
                if(Settings.OBS_ENABLE.get().getBoolean()) authenticate();
                else promptEnable();
                detectTask.cancel(true);
            } catch (Exception ex) {
                if(ex instanceof InterruptedException) return;
                if(ex instanceof ConnectException) {
                    promptInstall();
                    detectTask.cancel(true);
                    return;
                }
                ExceptionHandler.catchException(ex);
                detectTask.cancel(true);
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    public void authenticate() throws Exception {
        File key = new File(Beezig.get().getBeezigDir(), "obs_data");
        if (!key.exists()) refreshAuthKey();
        else {
            try (InputStream is = new FileInputStream(key)) {
                byte[] bytes = new byte[32];
                int read = is.read(bytes);
                if (bytes.length != read)
                    throw new IOException(String.format("Couldn't read the full private key, %d != %d", bytes.length, read));
                messageSigner = new Ed25519Sign(bytes);
            }
        }
        Message.info(Beezig.api().translate("msg.obs", "§a" + Message.translate("msg.connected")));
    }

    /**
     * Called when OBS is detected but OBS Controller isn't installed
     */
    private void promptInstall() {
        if(!Settings.OBS_PROMPT.get().getBoolean()) return;
        Message.info(Message.translate("msg.obs.prompt"));
        Message.info(Message.translate("msg.obs.prompt.install"));
        TextButton btn = new TextButton("btn.obs.prompt.install", "btn.obs.prompt.install.desc", "§a");
        btn.doRunCommand("/bobs install");
        sendButtons(btn);
    }

    /**
     * Called when OBS Controller is detected but the feature isn't enabled
     */
    private void promptEnable() {
        if(!Settings.OBS_PROMPT.get().getBoolean()) return;
        Message.info(Message.translate("msg.obs.prompt"));
        Message.info(Message.translate("msg.obs.prompt.enable"));
        TextButton btn = new TextButton("btn.obs.prompt.enable", "btn.obs.prompt.enable.desc", "§a");
        btn.doRunCommand("/bsettings obs_enable on");
        sendButtons(btn);
    }

    private void sendButtons(TextButton btn) {
        MessageComponent component = new MessageComponent(Message.infoPrefix());
        component.getSiblings().add(btn);
        TextButton dismiss = new TextButton("btn.daily.ext.dismiss", "btn.daily.ext.dismiss.desc", "§c");
        dismiss.doRunCommand("/bsettings obs_prompt off");
        component.getSiblings().add(new MessageComponent(" "));
        component.getSiblings().add(dismiss);
        Beezig.api().messagePlayerComponent(component, false);
    }

    public void refreshAuthKey() throws Exception {
        String pub = generatePubKey();
        JSONObject json = new JSONObject();
        json.put("uuid", APP_UUID.toString());
        json.put("name", "Beezig");
        json.put("public_key", pub);
        Message.info(Message.translate("msg.obs.auth"));
        ObsHttp.HttpRes res = ObsHttp.sendPost("/register", json.toString());
        if(res.getCode() == 409) {
            MessageComponent base = new MessageComponent(Message.errorPrefix() + Message.translate("error.obs.auth.conflict") + " ");
            TextButton btn = new TextButton("btn.obs.install.guide.name", "btn.obs.install.guide.desc", "§e");
            btn.getStyle().setOnClick(new MessageAction(MessageAction.Action.OPEN_URL, "https://go.beezig.eu/obs-controller-reset"));
            base.getSiblings().add(btn);
            Beezig.api().messagePlayerComponent(base, false);
            return;
        }
        JSONObject parsed = (JSONObject) PARSER.parse(res.getBody());
        setSecretKey(parsed.get("key").toString(), parsed.get("shared_public").toString());
    }

    public void cancelTasks() {
        if(!notAuthenticated()) Message.info(Beezig.api().translate("msg.obs", "§c" + Message.translate("msg.disconnected")));
        detectTask.cancel(true);
    }

    private String generatePubKey() {
        x25519Private = X25519.generatePrivateKey();
        try {
            return Base64.getEncoder().encodeToString(X25519.publicFromPrivate(x25519Private));
        } catch (InvalidKeyException e) {
            ExceptionHandler.catchException(e);
        }
        return null;
    }

    private void saveKey(byte[] data) {
        File key = new File(Beezig.get().getBeezigDir(), "obs_data");
        try(OutputStream stream = new FileOutputStream(key)) {
            stream.write(data);
        } catch (IOException e) {
            ExceptionHandler.catchException(e);
        }
    }

    private void setSecretKey(String encrypted, String serverPublic) throws Exception {
        Base64.Decoder dec = Base64.getDecoder();
        byte[] encryptedBytes = dec.decode(encrypted);
        byte[] serverPublicKey = dec.decode(serverPublic);
        byte[] sharedSecret = X25519.computeSharedSecret(x25519Private, serverPublicKey);
        Arrays.fill(x25519Private, (byte) 0);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(sharedSecret, "AES"), new GCMParameterSpec(128, new byte[12]));
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        messageSigner = new Ed25519Sign(decryptedBytes);
        saveKey(decryptedBytes);
        Arrays.fill(decryptedBytes, (byte) 0);
    }

    private String signMessage(String msg) throws GeneralSecurityException {
        if(msg.isEmpty()) msg = "obs-controller";
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(msg.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(messageSigner.sign(digest.digest()));
    }

    public boolean notAuthenticated() {
        return messageSigner == null;
    }

    public void startRecording(String fileName) {
        if(!Settings.OBS_ENABLE.get().getBoolean() || notAuthenticated()) return;
        try {
            if(isError(ObsHttp.sendPost("/recording/start", fileName, signMessage(fileName), APP_UUID.toString()).getCode())) return;
            startedRecording = true;
            if(Settings.OBS_FILE_ACTIONS.get().getBoolean()) {
                WorldTask.submit(() -> Message.info(Beezig.api().translate("msg.obs", Color.accent() + Message.translate("msg.obs.started"))));
            }
        } catch (Exception e) {
            ExceptionHandler.catchException(e);
            Message.error(Message.translate("error.obs"));
        }
    }

    public void stopRecordingIfStarted() {
        if(!startedRecording || notAuthenticated()) return;
        try {
            ObsHttp.HttpRes res = ObsHttp.sendPost("/recording/stop", "", signMessage(""), APP_UUID.toString());
            if(isError(res.getCode())) return;
            JSONObject json = (JSONObject) PARSER.parse(res.getBody());
            Object path = json.get("path");
            if(path != null) announceRecordingStopped(path.toString());
            startedRecording = false;
        } catch (Exception e) {
            ExceptionHandler.catchException(e);
            Message.error(Message.translate("error.obs"));
        }
    }

    private void announceRecordingStopped(String filePath) {
        File file = new File(FilenameUtils.normalize(filePath));
        String name = file.getName();
        UUID uuid = UUID.randomUUID();
        ObsFileOperations.insert(uuid, file);
        if(!Settings.OBS_FILE_ACTIONS.get().getBoolean()) return;
        MessageComponent main = new MessageComponent(Message.infoPrefix()
            + Beezig.api().translate("msg.obs.saved", Color.accent() + name + Color.primary()) + "\n");
        TextButton open = new TextButton("btn.obs.file.open", "btn.obs.file.open.desc", "§a");
        open.doRunCommand("/bobs open " + uuid);
        TextButton openFolder = new TextButton("btn.obs.file.open_folder", "btn.obs.file.open_folder.desc", "§b");
        openFolder.doRunCommand("/bobs folder " + uuid);
        TextButton delete = new TextButton("btn.obs.file.delete", "btn.obs.file.delete.desc", "§c");
        delete.doRunCommand("/bobs delete " + uuid);
        MessageComponent space = new MessageComponent(" ");
        main.getSiblings().add(new MessageComponent(Message.infoPrefix()));
        main.getSiblings().add(open);
        main.getSiblings().add(space);
        main.getSiblings().add(openFolder);
        main.getSiblings().add(space);
        main.getSiblings().add(delete);
        WorldTask.submit(() -> Beezig.api().messagePlayerComponent(main, false));
    }

    private boolean isError(int code) {
        if(code == 401) {
            Message.info(Message.translate("msg.obs.reauth"));
            return true;
        }
        return false;
    }
}
