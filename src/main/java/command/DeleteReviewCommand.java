package command;

import canteens.Canteen;
import exceptions.DukeExceptions;
import nusfoodreviews.NusFoodReviews;
import parser.Parser;
import reviews.Review;
import storage.Storage;
import storage.UpdateFile;
import stores.Store;
import ui.Ui;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static stores.Store.averageRating;
import static ui.Ui.LINESPACING;


public class DeleteReviewCommand extends Command {
    private NusFoodReviews nusFoodReviews;
    private Parser parser;

    public DeleteReviewCommand(NusFoodReviews nusFoodReviews, Parser parser) {
        this.nusFoodReviews = nusFoodReviews;
        this.parser = parser;
    }

    public void execute(ArrayList<Canteen> canteens, Ui ui) throws IOException, DukeExceptions {
        if (canteens.size() > 0) {
            nusFoodReviews.setCanteenIndex();
            int currentCanteenIndex = nusFoodReviews.getCanteenIndex();
            nusFoodReviews.setStoreIndex();
            int currentStoreIndex = nusFoodReviews.getStoreIndex();
            if (currentStoreIndex == -1) {
                return;
            }
            ArrayList<Store> stores = canteens.get(currentCanteenIndex).getStores();
            ArrayList<Review> reviews = stores.get(currentStoreIndex).getReviews();
            averageRating = stores.get(currentStoreIndex).getAverageRating();
            if (reviews.size() <= 0) {
                System.out.println(LINESPACING);
                System.out.println("There are currently no reviews in this store to delete!");
                System.out.println(LINESPACING);
                return;
            }
            ui.showReviews(stores.get(currentStoreIndex).getStoreName(), reviews, averageRating);
            ui.showDeleteReview();

            String line = ui.readCommand();
            if (line.equals("cancel")) {
                ui.showReviewNotDeleted();
                return;
            }
            int reviewNumber = parser.parseInt(line, 1,
                    canteens.get(currentCanteenIndex).getStore(currentStoreIndex).getRatingCount()) - 1;

            Canteen currentCanteen = canteens.get(currentCanteenIndex);
            Store store = currentCanteen.getStore(currentStoreIndex);
            store.deleteReview(reviewNumber);
            ui.reviewDeleted();
            UpdateFile.deleteObjInFile(new FileWriter(Storage.DEFAULT_STORAGE_FILEPATH), canteens);
        } else {
            System.out.println(LINESPACING);
            System.out.println("There are no canteens for you to delete reviews for any stores!");
            System.out.println(LINESPACING);
        }


    }

}
