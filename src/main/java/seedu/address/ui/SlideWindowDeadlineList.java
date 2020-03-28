package seedu.address.ui;

import java.time.LocalDateTime;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.Displayable;
import seedu.address.model.Name;
import seedu.address.model.deadline.Deadline;
import seedu.address.model.event.Event;
import seedu.address.model.module.Module;

/**
 * An UI component that displays information of selected {@code Event} and its deadline list
 * update of children node of slideWindowEvent happened here.
 */
public class SlideWindowDeadlineList extends UiPart<Region> {

    private static final String FXML = "SlideWindowDeadlineList.fxml";

    @FXML
    private ListView<Displayable> deadlineListView;
    @FXML
    private VBox slideEventCard;
    private SlideWindowEvent slideWindowEvent;


    @SuppressWarnings("unchecked")
    public SlideWindowDeadlineList(ObservableList<? extends Displayable> displayableList,
                                   SlideWindowEvent slideWindowEvent) {
        super(FXML);
        deadlineListView.setItems((ObservableList<Displayable>) displayableList);
        deadlineListView.setCellFactory(listView -> new ListViewCell());
        if (slideWindowEvent != null) {
            slideEventCard.getChildren().setAll(slideWindowEvent.getRoot());
        }
    }

    public Region getSlideEventCard() {
        return slideWindowEvent.getRoot();
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code event} using a {@code eventCard}.
     */
    static class ListViewCell extends ListCell<Displayable> {
        int index = 0;
        @Override
        protected void updateItem(Displayable listItem, boolean empty) {
            super.updateItem(listItem, empty);
            if (empty || listItem == null) {
                setGraphic(null);
                setText(null);
            } else if (listItem instanceof Event) {
                // Create a dummy deadline list to test for the slide window.
                //Deadline deadline = new Deadline(new Name("Read lecture 8 and do tutorial "),
                //        LocalDateTime.now().withNano(0), false);
                // setGraphic(new EventCard((Event)listItem, index++).getRoot());
                setGraphic(new EventCard((Event) listItem, getIndex() + 1).getRoot());
            } else if (listItem instanceof Module) {
                setGraphic(new ModuleCard((Module)listItem, getIndex() + 1).getRoot());
            } else if (listItem instanceof Deadline) {
                setGraphic(new DeadlineCard((Deadline) listItem, getIndex() + 1).getRoot());
            }
        }
    }

}
