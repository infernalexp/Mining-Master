/*
 * Copyright 2022 Infernal Studios
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

package org.infernalstudios.miningmaster.config.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.config.MiningMasterConfig;
import org.infernalstudios.miningmaster.gen.features.RandomGemOreFeature;
import org.infernalstudios.miningmaster.gen.features.RandomNetherGemOreFeature;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ConfigScreen extends Screen {
    private OptionsRowList optionsRowList;

    public ConfigScreen() {
        super(new TranslationTextComponent(MiningMaster.MOD_ID + ".config.title"));
    }

    @Override
    public void init() {
        optionsRowList = new OptionsRowList(minecraft, width, height, 24, height - 32, 25);

        // Gem Booleans
        optionsRowList.addOption(new BooleanOption(MiningMaster.MOD_ID + ".config.option.fireRubyEnabled",
                new TranslationTextComponent(MiningMaster.MOD_ID + ".config.tooltip.fireRubyEnabled"),
                settings -> MiningMasterConfig.CONFIG.fireRubyEnabled.get(), (settings, value) -> MiningMasterConfig.CONFIG.fireRubyEnabled.set(value)
        ));

        optionsRowList.addOption(new BooleanOption(MiningMaster.MOD_ID + ".config.option.iceSapphireEnabled",
                new TranslationTextComponent(MiningMaster.MOD_ID + ".config.tooltip.iceSapphireEnabled"),
                settings -> MiningMasterConfig.CONFIG.iceSapphireEnabled.get(), (settings, value) -> MiningMasterConfig.CONFIG.iceSapphireEnabled.set(value)
        ));

        optionsRowList.addOption(new BooleanOption(MiningMaster.MOD_ID + ".config.option.spiritGarnetEnabled",
                new TranslationTextComponent(MiningMaster.MOD_ID + ".config.tooltip.spiritGarnetEnabled"),
                settings -> MiningMasterConfig.CONFIG.spiritGarnetEnabled.get(), (settings, value) -> MiningMasterConfig.CONFIG.spiritGarnetEnabled.set(value)
        ));

        optionsRowList.addOption(new BooleanOption(MiningMaster.MOD_ID + ".config.option.hastePeridotEnabled",
                new TranslationTextComponent(MiningMaster.MOD_ID + ".config.tooltip.hastePeridotEnabled"),
                settings -> MiningMasterConfig.CONFIG.hastePeridotEnabled.get(), (settings, value) -> MiningMasterConfig.CONFIG.hastePeridotEnabled.set(value)
        ));

        optionsRowList.addOption(new BooleanOption(MiningMaster.MOD_ID + ".config.option.luckyCitrineEnabled",
                new TranslationTextComponent(MiningMaster.MOD_ID + ".config.tooltip.luckyCitrineEnabled"),
                settings -> MiningMasterConfig.CONFIG.luckyCitrineEnabled.get(), (settings, value) -> MiningMasterConfig.CONFIG.luckyCitrineEnabled.set(value)
        ));

        optionsRowList.addOption(new BooleanOption(MiningMaster.MOD_ID + ".config.option.diveAquamarineEnabled",
                new TranslationTextComponent(MiningMaster.MOD_ID + ".config.tooltip.diveAquamarineEnabled"),
                settings -> MiningMasterConfig.CONFIG.diveAquamarineEnabled.get(), (settings, value) -> MiningMasterConfig.CONFIG.diveAquamarineEnabled.set(value)
        ));

        optionsRowList.addOption(new BooleanOption(MiningMaster.MOD_ID + ".config.option.heartRhodoniteEnabled",
                new TranslationTextComponent(MiningMaster.MOD_ID + ".config.tooltip.heartRhodoniteEnabled"),
                settings -> MiningMasterConfig.CONFIG.heartRhodoniteEnabled.get(), (settings, value) -> MiningMasterConfig.CONFIG.heartRhodoniteEnabled.set(value)
        ));

        optionsRowList.addOption(new BooleanOption(MiningMaster.MOD_ID + ".config.option.powerPyriteEnabled",
                new TranslationTextComponent(MiningMaster.MOD_ID + ".config.tooltip.powerPyriteEnabled"),
                settings -> MiningMasterConfig.CONFIG.powerPyriteEnabled.get(), (settings, value) -> MiningMasterConfig.CONFIG.powerPyriteEnabled.set(value)
        ));

        optionsRowList.addOption(new BooleanOption(MiningMaster.MOD_ID + ".config.option.kineticOpalEnabled",
                new TranslationTextComponent(MiningMaster.MOD_ID + ".config.tooltip.kineticOpalEnabled"),
                settings -> MiningMasterConfig.CONFIG.kineticOpalEnabled.get(), (settings, value) -> MiningMasterConfig.CONFIG.kineticOpalEnabled.set(value)
        ));

        optionsRowList.addOption(new BooleanOption(MiningMaster.MOD_ID + ".config.option.airMalachiteEnabled",
                new TranslationTextComponent(MiningMaster.MOD_ID + ".config.tooltip.airMalachiteEnabled"),
                settings -> MiningMasterConfig.CONFIG.airMalachiteEnabled.get(), (settings, value) -> MiningMasterConfig.CONFIG.airMalachiteEnabled.set(value)
        ));

        // Common Gem Spawns
        optionsRowList.addOption(new SliderPercentageOption(MiningMaster.MOD_ID + ".config.option.commonGemsPerChunk", 0, 100, 1,
                        settings -> MiningMasterConfig.CONFIG.commonGemsPerChunk.get().doubleValue(), (settings, value) -> MiningMasterConfig.CONFIG.commonGemsPerChunk.set(value.intValue()),
                        (settings, option) -> {
                            option.setOptionValues(Minecraft.getInstance().fontRenderer.trimStringToWidth(
                                    new TranslationTextComponent(MiningMaster.MOD_ID + ".config.tooltip.commonGemsPerChunk"), 200));

                            return new TranslationTextComponent("options.generic_value", option.getBaseMessageTranslation(), // getBaseMessageTranslation() is protected by default, use an access transformer to be able to use it
                                    new StringTextComponent(Double.toString((double) Math.round(option.get(settings) * 100) / 100)));
                        }
                )
        );

        // Rare Gem Spawns
        optionsRowList.addOption(new SliderPercentageOption(MiningMaster.MOD_ID + ".config.option.rareGemsPerChunk", 0, 100, 1,
                        settings -> MiningMasterConfig.CONFIG.rareGemsPerChunk.get().doubleValue(), (settings, value) -> MiningMasterConfig.CONFIG.rareGemsPerChunk.set(value.intValue()),
                        (settings, option) -> {
                            option.setOptionValues(Minecraft.getInstance().fontRenderer.trimStringToWidth(
                                    new TranslationTextComponent(MiningMaster.MOD_ID + ".config.tooltip.rareGemsPerChunk"), 200));

                            return new TranslationTextComponent("options.generic_value", option.getBaseMessageTranslation(), // getBaseMessageTranslation() is protected by default, use an access transformer to be able to use it
                                    new StringTextComponent(Double.toString((double) Math.round(option.get(settings) * 100) / 100)));
                        }
                )
        );

        // Random Gem Spawns
        optionsRowList.addOption(new SliderPercentageOption(MiningMaster.MOD_ID + ".config.option.randomGemsPerChunk", 0, 100, 1,
                        settings -> MiningMasterConfig.CONFIG.randomGemsPerChunk.get().doubleValue(), (settings, value) -> MiningMasterConfig.CONFIG.randomGemsPerChunk.set(value.intValue()),
                        (settings, option) -> {
                            option.setOptionValues(Minecraft.getInstance().fontRenderer.trimStringToWidth(
                                    new TranslationTextComponent(MiningMaster.MOD_ID + ".config.tooltip.randomGemsPerChunk"), 200));

                            return new TranslationTextComponent("options.generic_value", option.getBaseMessageTranslation(), // getBaseMessageTranslation() is protected by default, use an access transformer to be able to use it
                                    new StringTextComponent(Double.toString((double) Math.round(option.get(settings) * 100) / 100)));
                        }
                )
        );

        children.add(optionsRowList);

        addButton(new Button((width - 200) / 2, height - 26, 200, 20, new TranslationTextComponent("gui.done"), button -> closeScreen()));
    }

    @Override
    public void closeScreen() {
        super.closeScreen();
        RandomNetherGemOreFeature.calculateEnabledOres();
        RandomGemOreFeature.calculateEnabledOres();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);

        optionsRowList.render(matrixStack, mouseX, mouseY, partialTicks);

        List<IReorderingProcessor> list = SettingsScreen.func_243293_a(optionsRowList, mouseX, mouseY);
        if (list != null) {
            this.renderTooltip(matrixStack, list, mouseX, mouseY);
        }

        // The parameter names for this function are wrong. The three integers at the end should be x, y, color
        drawCenteredString(matrixStack, font, title, width / 2, 8, 0xFFFFFF);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}
