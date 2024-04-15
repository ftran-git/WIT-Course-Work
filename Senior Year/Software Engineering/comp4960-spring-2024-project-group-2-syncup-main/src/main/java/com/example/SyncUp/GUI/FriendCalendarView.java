package com.example.SyncUp.GUI;

import com.example.SyncUp.Controllers.*;
import com.example.SyncUp.GUI.PopupViews.EventDetailsView;
import com.example.SyncUp.Objects.FriendData;
import com.example.SyncUp.RDFResources.*;
import com.inrupt.client.accessgrant.AccessGrant;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.context.ConfigurableApplicationContext;

public class FriendCalendarView extends View{

    private ArrayList<AnchorPaneNode> allCalendarDays = new ArrayList<>(35);
    private VBox view;
    private Text calendarTitle;
    private YearMonth currentYearMonth;
    private RESTController restController;
    private String resourceURL;
    private FriendData friendData;
    private String friendPodURL;
    public static final Color COLOR_FILL = Color.ALICEBLUE;

    /**
     * Create a calendar view for Friend
     * @param yearMonth year month to create the calendar of
     */
    public FriendCalendarView(Stage primaryStage, YearMonth yearMonth, ConfigurableApplicationContext applicationContext, FriendData friendData, String friendPodURL) {
        // Store FriendData object and friendPodURL for other utility methods
        this.friendData = friendData;
        this.friendPodURL = friendPodURL;

        restController = applicationContext.getBean(RESTController.class);

        currentYearMonth = yearMonth;
        // Create the calendar grid pane
        GridPane calendar = new GridPane();
        calendar.setPrefSize(600, 400);
        calendar.setGridLinesVisible(true);
        // Create rows and columns with anchor panes for the calendar
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                AnchorPaneNode ap = new AnchorPaneNode(primaryStage, applicationContext);
                ap.setPrefSize(200,200);
                calendar.add(ap,j,i);
                allCalendarDays.add(ap);
            }
        }
        // Days of the week labels
        Text[] dayNames = new Text[]{ new Text("Sunday"), new Text("Monday"), new Text("Tuesday"),
                new Text("Wednesday"), new Text("Thursday"), new Text("Friday"),
                new Text("Saturday") };
        GridPane dayLabels = new GridPane();
        dayLabels.setPrefWidth(600);
        Integer col = 0;
        for (Text txt : dayNames) {
            AnchorPane ap = new AnchorPane();
            ap.setPrefSize(200, 10);
            ap.setBottomAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            dayLabels.add(ap, col++, 0);
        }
        // Create calendarTitle and buttons to change current month
        calendarTitle = new Text();
        Button previousMonth = new Button("<<");
        previousMonth.setOnAction(e -> previousMonth(applicationContext));
        Button nextMonth = new Button(">>");
        nextMonth.setOnAction(e -> nextMonth(applicationContext));
        HBox titleBar = new HBox(previousMonth, calendarTitle, nextMonth);
        titleBar.setAlignment(Pos.BASELINE_CENTER);
        // TODO Populate calendar with the appropriate day numbers
        List<Event> events = eventsForMonth(yearMonth);
        populateCalendar(yearMonth, events, applicationContext);
        // Create bottom bar to display friend's name
        Label friendNameLabel = new Label("Calendar for " + friendData.toString() + "!");
        friendNameLabel.setFont(new Font(20));
        HBox bottomBar = new HBox(friendNameLabel);
        bottomBar.setAlignment(Pos.CENTER);
        // Create the calendar view
        view = new VBox(titleBar, dayLabels, calendar, bottomBar);
        view.setBackground(new Background(new BackgroundFill(COLOR_FILL, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    /**
     * Set the days of the calendar to correspond to the appropriate date
     * @param yearMonth year and month of month to render
     */
    public void populateCalendar(YearMonth yearMonth, List<Event> events, ConfigurableApplicationContext applicationContext) {
        // Get the date we want to start with on the calendar
        LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        // Dial back the day until it is SUNDAY (unless the month starts on a sunday)
        while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY") ) {
            calendarDate = calendarDate.minusDays(1);
        }
        // Populate the calendar with day numbers
        for (AnchorPaneNode ap : allCalendarDays) {
            if (ap.getChildren().size() != 0) {
                ap.getChildren().remove(0);
            }
            ap.getChildren().clear();
            Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));
            ap.setDate(calendarDate);
            ap.setTopAnchor(txt, 5.0);
            ap.setLeftAnchor(txt, 5.0);
            ap.getChildren().add(txt);

            double topAnchor = 20.0;
            for (Event event : events) {
                LocalDate startDate = event.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (startDate.equals(calendarDate)) {
                    Text eventText = new Text(event.getDescription());
                    eventText.setFill(Color.BLUE);
                    eventText.setUnderline(true);
                    eventText.setOnMouseClicked(e -> {
                        new EventDetailsView(new Stage(), event, applicationContext).show();
                        e.consume();
                    });
                    AnchorPane.setTopAnchor(eventText, topAnchor);
                    ap.getChildren().add(eventText);
                    topAnchor += 20.0; // Increment the top anchor for the next event
                }
            }

            calendarDate = calendarDate.plusDays(1);


        }

        // Change the title of the calendar
        calendarTitle.setText(yearMonth.getMonth().toString() + " " + String.valueOf(yearMonth.getYear()));
    }

    /**
     * Move the month back by one. Repopulate the calendar with the correct dates.
     */
    private void previousMonth(ConfigurableApplicationContext applicationContext) {
        currentYearMonth = currentYearMonth.minusMonths(1);
        List<Event> events = eventsForMonth(currentYearMonth);
        populateCalendar(currentYearMonth, events, applicationContext);
    }

    /**
     * Move the month forward by one. Repopulate the calendar with the correct dates.
     */
    private void nextMonth(ConfigurableApplicationContext applicationContext){
        currentYearMonth = currentYearMonth.plusMonths(1);
        List<Event> events = eventsForMonth(currentYearMonth);
        populateCalendar(currentYearMonth, events, applicationContext);
    }

    public VBox getView() {
        return view;
    }

    public ArrayList<AnchorPaneNode> getAllCalendarDays() {
        return allCalendarDays;
    }

    public void setAllCalendarDays(ArrayList<AnchorPaneNode> allCalendarDays) {
        this.allCalendarDays = allCalendarDays;
    }

    public List<Event> eventsForMonth(YearMonth yearMonth){
        // Get AccessGrant
        AccessGrant accessGrant = restController.getAccessGrant(friendData.getWebID().toString(), restController.solidWebID);

        // Use AccessGrant to grab Event container
        //Set<URI> resourceURLs = restController.getContainerFromFriend(accessGrant, friendPodURL + "events/");
        //System.out.println(resourceURLs);

        // list to be returned
        List<Event> events = new ArrayList<>();

        // path to month's container
        String path = friendPodURL + "events/" + String.format("%d%02d", yearMonth.getYear(), yearMonth.getMonthValue()) + "/";
        System.out.println("\n\n\nPATH: " + path);
        // ensure container for month exists
        try {
            Container container = new Container(new URI(path));
            restController.createContainerForFriend(accessGrant, container);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // Get all resources from specifed month container
        resourceURL = restController.getContainerFromFriend(accessGrant, path).toString();
        if (resourceURL == "[]"){
            return events;
        }
        resourceURL = resourceURL.substring(1, resourceURL.length()-1).replace(" ", "");
        String[] resources = resourceURL.split(",");

        for (int i = 0; i < resources.length; i++) {
            if (resources[i] != null){
                events.add(restController.getEventFromFriend(accessGrant, resources[i]));
            }
        }
        return events;
    }
}
