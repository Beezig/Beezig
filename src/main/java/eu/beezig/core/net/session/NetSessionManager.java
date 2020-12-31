/*
 * Copyright (C) 2017-2021 Beezig Team
 *
 * This file is part of Beezig.
 *
 * Beezig is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beezig is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Beezig.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.beezig.core.net.session;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import eu.beezig.core.Beezig;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class NetSessionManager {
    public static ISessionProvider provider;

    public void sendAuthRequest(int sharedSecret) throws NoSuchAlgorithmException, AuthenticationException {
        String hash = computeHash(sharedSecret);
        MinecraftSessionService yggdrasil = new YggdrasilAuthenticationService(provider.getProxy(), UUID.randomUUID().toString()).createMinecraftSessionService();
        yggdrasil.joinServer(Beezig.user(), provider.getSessionString(), hash);
    }

    private String computeHash(int sharedSecret) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        UUID uuid = Beezig.user().getId();
        digest.update("Beezig".getBytes(StandardCharsets.UTF_8));
        digest.update(ByteBuffer.allocate(Integer.BYTES).putInt(sharedSecret).array());
        digest.update(ByteBuffer.allocate(Long.BYTES * 2).putLong(uuid.getMostSignificantBits()).putLong(uuid.getLeastSignificantBits()).array());
        byte[] result = digest.digest();
        return new BigInteger(result).toString(16);
    }
}
