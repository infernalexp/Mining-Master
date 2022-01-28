/*
 * Copyright 2021 Infernal Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.infernalstudios.miningmaster.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.infernalstudios.miningmaster.containers.GemForgeContainer;

import java.util.function.Supplier;

public class UpdateGemForgePacket {
    private final boolean isActive;

    public UpdateGemForgePacket(boolean isActive) {
        this.isActive = isActive;
    }

    public static void encode(UpdateGemForgePacket message, PacketBuffer buffer) {
        buffer.writeBoolean(message.isActive);
    }

    public static UpdateGemForgePacket decode(PacketBuffer buffer) {
        return new UpdateGemForgePacket(buffer.readBoolean());
    }

    public static void handle(UpdateGemForgePacket message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            PlayerEntity playerEntity = context.get().getSender();

            if (playerEntity != null && playerEntity.getServer() != null && playerEntity.openContainer instanceof GemForgeContainer) {
                ((GemForgeContainer)playerEntity.openContainer).setForgeActive(message.isActive);
            }
        });

        context.get().setPacketHandled(true);
    }
}
