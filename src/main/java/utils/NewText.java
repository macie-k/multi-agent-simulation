package utils;

import app.Window;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class NewText extends StackPane {
	
	private final int max;
	
	private final Text name;
	private final Text stats;
	
	public NewText(String defaultVal, int x, int y, int max) {
		this.name = new Text(defaultVal + ": ");
		this.stats = new Text();
		this.max = max;
		
		name.getStyleClass().add("agent-stats-text");
		name.setTranslateX(0);
		stats.getStyleClass().add("agent-stats");
		stats.setTranslateX(150);
		
		setAlignment(Pos.CENTER_LEFT);
		setMaxSize(Window.PANEL_WIDTH, 30);
		setTranslateX(x);
		setTranslateY(y);
		setStats(0);
		
		getChildren().addAll(name, stats);
	}
	
	public void setStats(int val) {
		stats.setText(String.format("%d/%d", val, max));
	}
}
