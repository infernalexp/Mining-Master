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

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DamageKnightJumpPacket {
    private final int damage;

    public DamageKnightJumpPacket(int damage) {
        this.damage = damage;
    }

    public static void encode(DamageKnightJumpPacket message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.damage);
    }

    public static DamageKnightJumpPacket decode(FriendlyByteBuf buffer) {
        return new DamageKnightJumpPacket(buffer.readInt());
    }

    public static void handle(DamageKnightJumpPacket message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player playerEntity = context.get().getSender();

            if (playerEntity != null) {
                ItemStack stack = playerEntity.getItemBySlot(EquipmentSlot.LEGS);
                stack.hurtAndBreak(message.damage, playerEntity, (onBroken) -> onBroken.broadcastBreakEvent(EquipmentSlot.LEGS));
            }
        });

        context.get().setPacketHandled(true);
    }
}
