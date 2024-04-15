package com.example.SyncUp;

import com.example.SyncUp.GUI.View;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SyncUpApplication {

	public static void main(String[] args) {
		Application.launch(View.class, args);
	}

}