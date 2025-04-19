package com.example.bankorganizer;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.util.*;

@Slf4j
@PluginDescriptor(
    name = "Bank Organizer"
)
public class BankOrganizerPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private ClientThread clientThread;

    private static final Map<String, List<Integer>> TAB_ITEM_IDS = new LinkedHashMap<>();

    static {
        TAB_ITEM_IDS.put("Essentials", Arrays.asList(995, 12791, 13639, 11941, 8013, 1712, 2552, 3853, 11850));
        TAB_ITEM_IDS.put("Combat", Arrays.asList(4151, 11802, 12926, 11907, 11865, 3024, 6685, 385, 2434));
        TAB_ITEM_IDS.put("Skilling", Arrays.asList(6739));
    }

    private boolean organized = false;

    @Override
    protected void startUp() {
        log.info("Bank Organizer plugin started!");
        organized = false;
    }

    @Override
    protected void shutDown() {
        log.info("Bank Organizer plugin stopped.");
    }

    @Subscribe
    public void onGameTick(GameTick tick) {
        if (organized || client.getItemContainer(InventoryID.BANK) == null) return;

        clientThread.invokeLater(() -> {
            ItemContainer bank = client.getItemContainer(InventoryID.BANK);
            if (bank == null) return;

            int tabIndex = 1;

            for (Map.Entry<String, List<Integer>> entry : TAB_ITEM_IDS.entrySet()) {
                List<Integer> itemIds = entry.getValue();
                for (Item item : bank.getItems()) {
                    if (item == null || item.getId() <= 0) continue;

                    if (itemIds.contains(item.getId())) {
                        client.setBankTab(item.getId(), tabIndex);
                    }
                }
                tabIndex++;
            }

            organized = true;
        });
    }

    @Provides
    BankOrganizerConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(BankOrganizerConfig.class);
    }
}
