package command;

import canteens.Canteen;
import exceptions.DukeExceptions;
import menus.Menu;
import nusfoodreviews.NusFoodReviews;
import parser.Parser;
import storage.Storage;
import stores.Store;
import ui.Ui;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;



public class DeleteMenuCommand extends Command {
    private NusFoodReviews nusFoodReviews;
    private Parser parser;

    public DeleteMenuCommand(NusFoodReviews nusFoodReviews, Parser parser) {
        this.nusFoodReviews = nusFoodReviews;
        this.parser = parser;
    }

    public void execute(ArrayList<Canteen> canteens, Ui ui) throws IOException, DukeExceptions {
        nusFoodReviews.setCanteenIndex();
        int currentCanteenIndex = nusFoodReviews.getCanteenIndex();
        if (currentCanteenIndex == -1) {
            ui.showMenuNotDeleted();
            return;
        }
        ui.showDisplayStores(canteens.get(currentCanteenIndex));
        ui.chooseStore();

        String line = ui.readCommand();
        if (line.equals("cancel")) {
            ui.showMenuNotDeleted();
            return;
        }
        int currentStoreIndex = Integer.parseInt(line) - 1;
        ArrayList<Menu> menus = canteens.get(currentCanteenIndex).getStore(currentStoreIndex).getMenus();
        ui.showDisplayMenu(canteens.get(currentCanteenIndex).getStore(currentStoreIndex).getStoreName(),
                menus);
        ui.chooseMenu();

        line = ui.readCommand();
        if (line.equals("cancel")) {
            ui.showMenuNotDeleted();
            return;
        }
        int menuNumber = parser.parseInt(line,1,
                canteens.get(currentCanteenIndex).getStore(currentStoreIndex).getMenuCount()) - 1;

        Canteen currentCanteen = canteens.get(currentCanteenIndex);
        Store store = currentCanteen.getStore(currentStoreIndex);
        String menuName = store.getMenus().get(menuNumber).getItemName();
        store.deleteMenu(menuNumber);
        ui.menuDeleted(menuName);
        Storage.save(new FileWriter("data/storage.txt"),canteens);
    }

}

