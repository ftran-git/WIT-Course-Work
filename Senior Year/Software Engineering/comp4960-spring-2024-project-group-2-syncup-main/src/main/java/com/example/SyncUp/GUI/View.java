package com.example.SyncUp.GUI;

import java.io.FileInputStream;
import java.net.URI;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Set;

import com.example.SyncUp.Controllers.RESTController;
import com.example.SyncUp.GUI.PopupViews.ErrorView;
import com.example.SyncUp.Objects.FriendData;
import com.example.SyncUp.Objects.Team;
import com.example.SyncUp.Objects.User;
import com.example.SyncUp.SyncUpApplication;
import com.inrupt.client.accessgrant.AccessGrant;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

public class View extends Application{

	public static final Color HEADER_COLOR = Color.BLACK;
	public static final Color COLOR_FILL = Color.ALICEBLUE;
	protected static User CURRENT_USER = null;
	private static String CURRENT_SCENE = "login";
	public static final int SCENE_WIDTH = 1000;
	public static final int SCENE_HEIGHT = 600;

	public static void main(String[] args){
		launch(args);
	}

	/** Spring Framework Integration **/
	private static ConfigurableApplicationContext applicationContext;
	static class StageReadyEvent extends ApplicationEvent {
		public StageReadyEvent(Stage stage) {
			super(stage);
		}
		public Stage getStage() {
			return ((Stage) getSource());
		}
	}
	@Override
	public void init() {
		applicationContext = new SpringApplicationBuilder(SyncUpApplication.class).run();
	}
	@Override
	public void stop() {
		applicationContext.close();
		Platform.exit();
	}

	/** Initialize View **/
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Allows other beans in Spring application context to communicate if needed
		applicationContext.publishEvent(new StageReadyEvent(primaryStage));

		/* scenes:
		 *
		 *  getLogin()
		 *  getCalendarView()
		 *  getTeamView()
		 *
		 */
		primaryStage.setScene(getLoginView(primaryStage));
		primaryStage.show();
	}

	/***
	 *
	 * @return
	 */
	public static HBox getHeader(Stage primaryStage) {
		HBox header = new HBox(100);
		header.prefWidthProperty().bind(primaryStage.widthProperty());
		header.setBackground(new Background(new BackgroundFill(HEADER_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

		Text syncUp = new Text("SyncUp!");
		syncUp.setFont(Font.font ("Impact", 30));
		syncUp.setFill(Color.WHITE);

		StackPane textContainer = new StackPane(syncUp);
		textContainer.setAlignment(Pos.CENTER_LEFT);

		header.getChildren().add(textContainer);

//		if(!CURRENT_SCENE.equals("login")){
//			try {
//				StackPane stack = new StackPane();
//				Image image = new Image(new FileInputStream("media/logout.png"));
//				ImageView iv2 = new ImageView();
//				iv2.setImage(image);
//				iv2.setFitWidth(25);
//				iv2.setPreserveRatio(true);
//				stack.getChildren().addAll(iv2);
//				stack.setAlignment(Pos.CENTER_RIGHT);
//				//HBox.setHgrow(iv2, Priority.ALWAYS);
//				stack.setMaxWidth(50);
//				header.setMargin(stack, new Insets(0, 10, 0, 0));
//
//				header.getChildren().add(stack);
//				stack.setOnMouseClicked(e->{
//					CURRENT_USER = null;
//					Stage currentStage = (Stage)primaryStage.getScene().getWindow();
//					currentStage.setScene(getLoginView(primaryStage));
//				});
//
//			} catch (Exception e){
//				System.out.println("Image URL not found");
//			}
//		}
		if(!CURRENT_SCENE.equals("login")){
			Button logoutButton = new Button("Log Out");
			header.getChildren().add(logoutButton);
			EventHandler<ActionEvent> logout = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					CURRENT_USER = null;
					Stage currentStage = (Stage)primaryStage.getScene().getWindow();
					currentStage.setScene(getLoginView(primaryStage));
				}
			};
			logoutButton.setOnAction(logout);
		}

		HBox.setHgrow(textContainer, Priority.ALWAYS);
		textContainer.setMaxWidth(Double.MAX_VALUE);

		return header;
	}

	/***
	 *
	 * @param primaryStage
	 * @return
	 */
	public static HBox getNavigation(Stage primaryStage) {
		HBox navigation = new HBox(100);
		navigation.setBackground(new Background(new BackgroundFill(HEADER_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		navigation.setPrefWidth(400);
		//fill in with menu options
		//
		Text profile_nav = new Text("Profile");
		profile_nav.setFont(Font.font ("Impact", 20));
		profile_nav.setFill(Color.WHITE);
		profile_nav.setOnMouseClicked(e -> {
			Stage currentStage = (Stage)primaryStage.getScene().getWindow();
			currentStage.setScene(getProfileView(primaryStage));
		});
		//
		Text calendar_nav = new Text("Calendar");
		calendar_nav.setFont(Font.font ("Impact", 20));
		calendar_nav.setFill(Color.WHITE);
		calendar_nav.setOnMouseClicked(e -> {
			Stage currentStage = (Stage)primaryStage.getScene().getWindow();
			currentStage.setScene(getCalendarView(primaryStage));
		});

		//
		Text friend_nav = new Text("Friends");
		friend_nav.setFont(Font.font ("Impact", 20));
		friend_nav.setFill(Color.WHITE);
		friend_nav.setOnMouseClicked(e -> {
			Stage currentStage = (Stage)primaryStage.getScene().getWindow();
			currentStage.setScene(getFriendsView(primaryStage));
		});

		//
		Text team_nav = new Text("Teams");
		team_nav.setFont(Font.font ("Impact", 20));
		team_nav.setFill(Color.WHITE);
		team_nav.setOnMouseClicked(e -> {
			Stage currentStage = (Stage)primaryStage.getScene().getWindow();
			currentStage.setScene(getTeamView(primaryStage, CURRENT_USER.getTeams()));
		});

		navigation.getChildren().addAll(profile_nav, calendar_nav, friend_nav, team_nav);
		navigation.setAlignment(Pos.CENTER);

		return navigation;
	}

	/***
	 *
	 * @param primaryStage
	 * @return
	 */
	public static Scene getLoginView(Stage primaryStage) {
		CURRENT_SCENE = "login";

		// Create border pane for layout to add SyncUp header
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(getHeader(primaryStage));
		GridPane login = new LoginView(primaryStage, applicationContext).getView();
		borderPane.setCenter(login);

		// Return scene
		return new Scene(borderPane, SCENE_WIDTH, SCENE_HEIGHT);
	}

	/***
	 * Grabs profile view
	 * @param primaryStage
	 * @return
	 */
	public static Scene getProfileView(Stage primaryStage) {
		CURRENT_SCENE = "profile";

		VBox profile_view = new VBox(100);
		profile_view.setBackground(new Background(new BackgroundFill(COLOR_FILL, CornerRadii.EMPTY, Insets.EMPTY)));
		VBox profile = new ProfileView().getView();
		profile_view.getChildren().addAll(getHeader(primaryStage), getNavigation(primaryStage), profile);
		profile_view.setSpacing(0);
		return new Scene(profile_view, SCENE_WIDTH, SCENE_HEIGHT);
	}

	/***
	 * Grabs calendar view
	 * @param primaryStage
	 * @return
	 */
	public static Scene getCalendarView(Stage primaryStage) {
		CURRENT_SCENE = "calendar";
		VBox calendar_view = new VBox(100);
		calendar_view.setBackground(new Background(new BackgroundFill(COLOR_FILL, CornerRadii.EMPTY, Insets.EMPTY)));
		VBox calendar = new CalendarView(primaryStage, YearMonth.now(), applicationContext).getView();
		calendar_view.getChildren().addAll(getHeader(primaryStage), getNavigation(primaryStage), calendar);
		calendar_view.setSpacing(0);
		return new Scene(calendar_view, SCENE_WIDTH, SCENE_HEIGHT);
	}

	/***
	 * Grabs firends view
	 * @param primaryStage
	 * @return
	 */
	public static Scene getFriendsView(Stage primaryStage){
		VBox friendsView = new VBox(100);
		FriendsListView friendsListView = new FriendsListView(primaryStage, applicationContext);
		GridPane friends = friendsListView.getView();
		friendsView.getChildren().addAll(getHeader(primaryStage), getNavigation(primaryStage), friends);
		friendsView.setSpacing(0);
		return new Scene(friendsView, SCENE_WIDTH, SCENE_HEIGHT);
	}

	/***
	 *
	 * @param primaryStage
	 * @param teamsApartOf
	 * @return
	 */
	public static Scene getTeamView(Stage primaryStage, ArrayList<Team> teamsApartOf) {
		CURRENT_SCENE = "teams";
		VBox teams_pane = new VBox(100);
		teams_pane.setBackground(new Background(new BackgroundFill(COLOR_FILL, CornerRadii.EMPTY, Insets.EMPTY)));

		HBox teams_title = new HBox(50);
		//customize font and size
		Text your_teams = new Text("Your Teams");
		your_teams.setFont(Font.font ("Impact", 20));
		teams_title.getChildren().add(your_teams);
		teams_title.setMargin(your_teams, new Insets(20, 0, 20, 0));
		teams_pane.setAlignment(Pos.CENTER);

		HBox teams = new HBox(100);
		// fix spacing
		for (int i = 0; i<teamsApartOf.size(); i++) {
			VBox team = new VBox(10);
			Text team_name = new Text(teamsApartOf.get(i).getTeamName());
			team_name.setFont(Font.font(null, 15));
			Text members_text = new Text("Members");
			members_text.setFont(Font.font(null, FontWeight.BOLD, 12));
			team.getChildren().addAll(team_name, members_text);
			for (int j = 0; j<teamsApartOf.get(i).getTeamMembers().size(); j++) {
				Text member_name = new Text(teamsApartOf.get(i).getTeamMembers().get(j).getName());
				team.getChildren().add(member_name);
			}
			teams.getChildren().add(team);
		}

		teams_pane.getChildren().addAll(getHeader(primaryStage), getNavigation(primaryStage), teams_title, teams);
		teams_pane.setAlignment(Pos.TOP_CENTER);
		teams_pane.setSpacing(0);
		Scene teams_scene = new Scene(teams_pane, SCENE_WIDTH, SCENE_HEIGHT);

		return teams_scene;
	}

	/***
	 * Grabs calendar view for friends which is similar to regular calendar view with slight modifications
	 * @param primaryStage
	 * @return
	 */
	public static Scene getFriendCalendarView(Stage primaryStage, FriendData friendData, String friendPodURL) {
		CURRENT_SCENE = "friend_calendar";
		VBox calendar_view = new VBox(100);
		calendar_view.setBackground(new Background(new BackgroundFill(COLOR_FILL, CornerRadii.EMPTY, Insets.EMPTY)));
		VBox calendar = new FriendCalendarView(primaryStage, YearMonth.now(), applicationContext, friendData, friendPodURL).getView();
		calendar_view.getChildren().addAll(getHeader(primaryStage), getNavigation(primaryStage), calendar);
		calendar_view.setSpacing(0);
		return new Scene(calendar_view, SCENE_WIDTH, SCENE_HEIGHT);
	}
}