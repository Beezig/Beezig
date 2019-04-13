/*
 * Copyright 2017 John Grosh (john.a.grosh@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jagrosh.discordipc;

import com.jagrosh.discordipc.entities.*;
import com.jagrosh.discordipc.entities.Packet.OpCode;
import com.jagrosh.discordipc.entities.pipe.Pipe;
import com.jagrosh.discordipc.entities.pipe.PipeStatus;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.Closeable;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;

/**
 * Represents a Discord IPC Client that can send and receive
 * Rich Presence data.<p>
 *
 * The ID provided should be the <b>client ID of the particular
 * application providing Rich Presence</b>, which can be found
 * <a href=https://discordapp.com/developers/applications/me>here</a>.<p>
 *
 * When initially created using {@link #IPCClient(long)} the client will
 * be inactive awaiting a call to {@link #connect(DiscordBuild...)}.<br>
 * After the call, this client can send and receive Rich Presence data
 * to and from discord via {@link #sendRichPresence(RichPresence)} and
 * {@link #setListener(IPCListener)} respectively.<p>
 *
 * Please be mindful that the client created is initially unconnected,
 * and calling any methods that exchange data between this client and
 * Discord before a call to {@link #connect(DiscordBuild...)} will cause
 * an {@link IllegalStateException} to be thrown.<br>
 * This also means that the IPCClient cannot tell whether the client ID
 * provided is valid or not before a handshake.
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public final class IPCClient implements Closeable
{

    private final long clientId;
    private final HashMap<String,Callback> callbacks = new HashMap<>();
    private volatile Pipe pipe;
    private IPCListener listener = null;
    private Thread readThread = null;
    
    /**
     * Constructs a new IPCClient using the provided {@code clientId}.<br>
     * This is initially unconnected to Discord.
     *
     * @param clientId The Rich Presence application's client ID, which can be found
     *                 <a href=https://discordapp.com/developers/applications/me>here</a>
     */
    public IPCClient(long clientId)
    {
        this.clientId = clientId;
    }
    
    /**
     * Sets this IPCClient's {@link IPCListener} to handle received events.<p>
     *
     * A single IPCClient can only have one of these set at any given time.<br>
     * Setting this {@code null} will remove the currently active one.<p>
     *
     * This can be set safely before a call to {@link #connect(DiscordBuild...)}
     * is made.
     *
     * @param listener The {@link IPCListener} to set for this IPCClient.
     *
     * @see IPCListener
     */
    public void setListener(IPCListener listener)
    {
        this.listener = listener;
        if (pipe != null)
            pipe.setListener(listener);
    }
    
    /**
     * Opens the connection between the IPCClient and Discord.<p>
     *
     * <b>This must be called before any data is exchanged between the
     * IPCClient and Discord.</b>
     *
     * @param preferredOrder the priority order of client builds to connect to
     *
     * @throws IllegalStateException
     *         There is an open connection on this IPCClient.
     * @throws NoDiscordClientException
     *         No client of the provided {@link DiscordBuild build type}(s) was found.
     */
    public void connect(DiscordBuild... preferredOrder) throws NoDiscordClientException
    {
        checkConnected(false);

        callbacks.clear();
        pipe = null;

        pipe = Pipe.openPipe(this, clientId, callbacks, preferredOrder);



        if(listener != null)
            listener.onReady(this, pipe.user);

        startReading();
    }
    
    /**
     * Sends a {@link RichPresence} to the Discord client.<p>
     *
     * This is where the IPCClient will officially display
     * a Rich Presence in the Discord client.<p>
     *
     * Sending this again will overwrite the last provided
     * {@link RichPresence}.
     *
     * @param presence The {@link RichPresence} to send.
     *
     * @throws IllegalStateException
     *         If a connection was not made prior to invoking
     *         this method.
     *
     * @see RichPresence
     */
    public void sendRichPresence(RichPresence presence)
    {
        sendRichPresence(presence, null);
    }
    
    /**
     * Sends a {@link RichPresence} to the Discord client.<p>
     *
     * This is where the IPCClient will officially display
     * a Rich Presence in the Discord client.<p>
     *
     * Sending this again will overwrite the last provided
     * {@link RichPresence}.
     *
     * @param presence The {@link RichPresence} to send.
     * @param callback A {@link Callback} to handle success or error
     *
     * @throws IllegalStateException
     *         If a connection was not made prior to invoking
     *         this method.
     *
     * @see RichPresence
     */
    public void sendRichPresence(RichPresence presence, Callback callback)
    {
        checkConnected(true);

        JSONObject j = new JSONObject();
        JSONObject j2 = new JSONObject();
        JSONObject presenceJ = presence == null ? null : presence.toJson();
        j2.put("activity", presenceJ);
        j2.put("pid",getPID());
        j.put("cmd", "SET_ACTIVITY");
        j.put("args", j2);


        pipe.send(OpCode.FRAME, j , callback);
    }

    public void sendResult(int result, String userId) {
        checkConnected(true);

        JSONObject outer = new JSONObject();

        // https://github.com/discordapp/discord-rpc/blob/master/src/serialization.cpp#L230
        outer.put("cmd", result == 0 ? "SEND_ACTIVITY_JOIN_INVITE" : "CLOSE_ACTIVITY_JOIN_REQUEST");

        JSONObject args = new JSONObject();
        args.put("user_id", userId);

        outer.put("args", args);

        pipe.send(OpCode.FRAME, outer, null);
    }

    /**
     * Adds an event {@link Event} to this IPCClient.<br>
     * If the provided {@link Event} is added more than once,
     * it does nothing.
     * Once added, there is no way to remove the subscription
     * other than {@link #close() closing} the connection
     * and creating a new one.
     *
     * @param sub The event {@link Event} to add.
     *
     * @throws IllegalStateException
     *         If a connection was not made prior to invoking
     *         this method.
     */
    public void subscribe(Event sub)
    {
        subscribe(sub, null);
    }
    
    /**
     * Adds an event {@link Event} to this IPCClient.<br>
     * If the provided {@link Event} is added more than once,
     * it does nothing.
     * Once added, there is no way to remove the subscription
     * other than {@link #close() closing} the connection
     * and creating a new one.
     *
     * @param sub The event {@link Event} to add.
     * @param callback The {@link Callback} to handle success or failure
     *
     * @throws IllegalStateException
     *         If a connection was not made prior to invoking
     *         this method.
     */
    public void subscribe(Event sub, Callback callback)
    {
        checkConnected(true);
        if(!sub.isSubscribable())
            throw new IllegalStateException("Cannot subscribe to "+sub+" event!");

        JSONObject j = new JSONObject();
        j.put("cmd", "SUBSCRIBE");
        j.put("evt", sub.name());
        pipe.send(OpCode.FRAME, j, callback);
    }

    /**
     * Gets the IPCClient's current {@link PipeStatus}.
     *
     * @return The IPCClient's current {@link PipeStatus}.
     */
    public PipeStatus getStatus()
    {
        if (pipe == null) return PipeStatus.UNINITIALIZED;

        return pipe.getStatus();
    }

    /**
     * Attempts to close an open connection to Discord.<br>
     * This can be reopened with another call to {@link #connect(DiscordBuild...)}.
     *
     * @throws IllegalStateException
     *         If a connection was not made prior to invoking
     *         this method.
     */
    @Override
    public void close()
    {
        checkConnected(true);

        try {
            pipe.close();
        } catch (IOException e) {

        }
    }

    /**
     * Gets the IPCClient's {@link DiscordBuild}.<p>
     *
     * This is always the first specified DiscordBuild when
     * making a call to {@link #connect(DiscordBuild...)},
     * or the first one found if none or {@link DiscordBuild#ANY}
     * is specified.<p>
     *
     * Note that specifying ANY doesn't mean that this will return
     * ANY. In fact this method should <b>never</b> return the
     * value ANY.
     *
     * @return The {@link DiscordBuild} of this IPCClient, or null if not connected.
     */
    public DiscordBuild getDiscordBuild()
    {
        if (pipe == null) return null;

        return pipe.getDiscordBuild();
    }

    /**
     * Constants representing events that can be subscribed to
     * using {@link #subscribe(Event)}.<p>
     *
     * Each event corresponds to a different function as a
     * component of the Rich Presence.<br>
     * A full breakdown of each is available
     * <a href=https://discordapp.com/developers/docs/rich-presence/how-to>here</a>.
     */
    public enum Event
    {
        NULL(false), // used for confirmation
        READY(false),
        ERROR(false),
        ACTIVITY_JOIN(true),
        ACTIVITY_SPECTATE(true),
        ACTIVITY_JOIN_REQUEST(true),
        /**
         * A backup key, only important if the
         * IPCClient receives an unknown event
         * type in a JSON payload.
         */
        UNKNOWN(false);
        
        private final boolean subscribable;
        
        Event(boolean subscribable)
        {
            this.subscribable = subscribable;
        }
        
        public boolean isSubscribable()
        {
            return subscribable;
        }
        
        static Event of(String str)
        {
            if(str==null)
                return NULL;
            for(Event s : Event.values())
            {
                if(s != UNKNOWN && s.name().equalsIgnoreCase(str))
                    return s;
            }
            return UNKNOWN;
        }
    }


    // Private methods
    
    /**
     * Makes sure that the client is connected (or not) depending on if it should
     * for the current state.
     *
     * @param connected Whether to check in the context of the IPCClient being
     *                  connected or not.
     */
    private void checkConnected(boolean connected)
    {
        if(connected && getStatus() != PipeStatus.CONNECTED)
            throw new IllegalStateException(String.format("IPCClient (ID: %d) is not connected!", clientId));
        if(!connected && getStatus() == PipeStatus.CONNECTED)
            throw new IllegalStateException(String.format("IPCClient (ID: %d) is already connected!", clientId));
    }
    
    /**
     * Initializes this IPCClient's {@link IPCClient#readThread readThread}
     * and calls the first {@link Pipe#read()}.
     */
    private void startReading()
    {
        readThread = new Thread(() -> {
            try
            {
                Packet p;
                while((p = pipe.read()).getOp() != OpCode.CLOSE)
                {
                    JSONObject json = p.getJson();
                    Event event = Event.of((String)json.getOrDefault("evt", null));
                    String nonce = (String) json.getOrDefault("nonce", null);

                    switch(event)
                    {
                        case NULL:
                            if(nonce != null && callbacks.containsKey(nonce))
                                callbacks.remove(nonce).succeed(p);
                            break;

                        case ERROR:
                            if(nonce != null && callbacks.containsKey(nonce))
                                callbacks.remove(nonce).fail((String)((JSONObject)json.get("data")).getOrDefault("message", null));
                            break;
                            
                        case ACTIVITY_JOIN:

                            break;
                            
                        case ACTIVITY_SPECTATE:

                            break;
                            
                        case ACTIVITY_JOIN_REQUEST:

                            break;
                            
                        case UNKNOWN:

                            break;
                    }
                    if(listener != null && json.containsKey("cmd") && ((String)json.get("cmd")).equals("DISPATCH"))
                    {
                        try
                        {
                            JSONObject data =(JSONObject) json.get("data");
                            switch(Event.of((String)json.get("evt")))
                            {


                                case ACTIVITY_JOIN:
                                    listener.onActivityJoin(this, (String)data.get("secret"));
                                    break;
                                    
                                case ACTIVITY_SPECTATE:
                                    listener.onActivitySpectate(this, (String) data.get("secret"));
                                    break;
                                    
                                case ACTIVITY_JOIN_REQUEST:
                                    JSONObject u = (JSONObject) data.get("user");
                                    User user = new User(
                                            (String) u.get("username"),
                                            (String) u.get("discriminator"),
                                        Long.parseLong((String)u.get("id")),
                                            (String) u.getOrDefault("avatar", null)
                                    );
                                    listener.onActivityJoinRequest(this, (String) data.getOrDefault("secret", null), user);
                                    break;
                            }
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                pipe.setStatus(PipeStatus.DISCONNECTED);
                if(listener != null)
                    listener.onClose(this, p.getJson());
            }
            catch(IOException | ParseException ex) {
                ex.printStackTrace();

                pipe.setStatus(PipeStatus.DISCONNECTED);
                if (listener != null)
                    listener.onDisconnect(this, ex);
            }

        });


        readThread.start();
    }
    
    // Private static methods
    
    /**
     * Finds the current process ID.
     *
     * @return The current process ID.
     */
    private static int getPID()
    {
        String pr = ManagementFactory.getRuntimeMXBean().getName();
        return Integer.parseInt(pr.substring(0,pr.indexOf('@')));
    }
}
