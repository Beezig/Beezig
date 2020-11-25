package eu.beezig.core.util.obs;

import com.google.crypto.tink.subtle.Ed25519Sign;
import com.google.crypto.tink.subtle.X25519;
import eu.beezig.core.Beezig;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.text.Message;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

public class ObsState {
    private static final UUID APP_UUID = UUID.fromString("3552936e-4224-48d0-b0e8-b0f08be197f9");
    private byte[] x25519Private;
    private Ed25519Sign messageSigner;
    private boolean startedRecording;

    public ObsState() throws Exception {
        File key = new File(Beezig.get().getBeezigDir(), "obs_data");
        if(!key.exists()) refreshAuthKey();
        else {
            try(InputStream is = new FileInputStream(key)) {
                byte[] bytes = new byte[32];
                int read = is.read(bytes);
                if(bytes.length != read) throw new IOException(String.format("Couldn't read the full private key, %d != %d", bytes.length, read));
                messageSigner = new Ed25519Sign(bytes);
            }
        }
    }

    public void refreshAuthKey() throws Exception {
        String pub = generatePubKey();
        JSONObject json = new JSONObject();
        json.put("uuid", APP_UUID.toString());
        json.put("name", "Beezig");
        json.put("public_key", pub);
        ObsHttp.HttpRes res = ObsHttp.sendPost("/register", json.toString());
        JSONParser parser = new JSONParser();
        JSONObject parsed = (JSONObject) parser.parse(res.getBody());
        setSecretKey(parsed.get("key").toString(), parsed.get("shared_public").toString());
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
        if(notAuthenticated()) return;
        try {
            if(!checkStatusCode(ObsHttp.sendPost("/recording/start", fileName, signMessage(fileName), APP_UUID.toString()).getCode())) return;
            startedRecording = true;
        } catch (Exception e) {
            ExceptionHandler.catchException(e);
            Message.error(Message.translate("error.obs"));
        }
    }

    public void stopRecordingIfStarted() {
        if(!startedRecording || notAuthenticated()) return;
        try {
            checkStatusCode(ObsHttp.sendPost("/recording/stop", "", signMessage(""), APP_UUID.toString()).getCode());
            startedRecording = false;
        } catch (Exception e) {
            ExceptionHandler.catchException(e);
            Message.error(Message.translate("error.obs"));
        }
    }

    private boolean checkStatusCode(int code) {
        if(code == 401) {
            Message.info(Message.translate("msg.obs.reauth"));
            return false;
        }
        return true;
    }
}
