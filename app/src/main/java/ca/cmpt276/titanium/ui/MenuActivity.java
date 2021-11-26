package ca.cmpt276.titanium.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.UUID;

import ca.cmpt276.titanium.R;
import ca.cmpt276.titanium.model.ChildManager;
import ca.cmpt276.titanium.ui.coin_flip.CoinFlipActivity;
import ca.cmpt276.titanium.ui.tasks.TasksActivity;
import ca.cmpt276.titanium.ui.timer.TimerActivity;

/**
 * This activity represents the main menu.
 * Shows children and buttons to the timer and coin flip.
 */
public class MenuActivity extends AppCompatActivity {
    private ChildManager childManager;
    private TextView welcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.childManager = ChildManager.getInstance(this);

        Button addChildButton = findViewById(R.id.MaterialButton_menu_add_child);
        addChildButton.setOnClickListener(view -> startActivity(ChildActivity.makeIntent(this, getString(R.string.title_add_child), null)));

        Button coinFlipButton = findViewById(R.id.MaterialButton_menu_coin_flip);
        coinFlipButton.setOnClickListener(view -> startActivity(CoinFlipActivity.makeIntent(this)));

        Button timerButton = findViewById(R.id.MaterialButton_menu_timer);
        timerButton.setOnClickListener(view -> startActivity(TimerActivity.makeIntent(this)));

        Button whoseTurnButton = findViewById(R.id.MaterialButton_menu_tasks);
        whoseTurnButton.setOnClickListener(view -> startActivity(TasksActivity.makeIntent(this)));

        MaterialButton helpScreen = findViewById(R.id.MaterialButton_menu_help);
        helpScreen.setOnClickListener(view -> startActivity(HelpActivity.makeIntent(this)));

        welcomeMessage = findViewById(R.id.TextView_menu_welcome_message);
        setMessage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayChildren();
        setMessage();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.menu_item_child_edit_child) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void displayChildren() {
        if (childManager.getChildren().size() == 0) {
            findViewById(R.id.TextView_menu_empty_state_message).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.TextView_menu_empty_state_message).setVisibility(View.INVISIBLE);
        }

        ListView childrenListView = (ListView) findViewById(R.id.ListView_menu_children);
        MenuChildAdapter adapter = new MenuChildAdapter(this, childManager.getChildren());
        childrenListView.setAdapter(adapter);
        childrenListView.setClickable(true);

        childrenListView.setOnItemClickListener((parent, view, position, id) -> {
            UUID childUUID = childManager.getChildren().get(position).getUniqueID();
            Intent viewChildIntent = ChildActivity.makeIntent(this, getString(R.string.title_view_child), childUUID);

            startActivity(viewChildIntent);
        });
    }

    // setMessage function written from the following resource:
    // https://stackoverflow.com/questions/27589701/showing-morning-afternoon-evening-night-message-based-on-time-in-java
    private void setMessage() {
        Calendar currentCalendar = Calendar.getInstance();
        int currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);

        if (currentHour < 12) {
            welcomeMessage.setText(getString(R.string.subtitle_menu_morning));
        } else if (currentHour < 16) {
            welcomeMessage.setText(getString(R.string.subtitle_menu_afternoon));
        } else if (currentHour >= 17) {
            welcomeMessage.setText(getString(R.string.subtitle_menu_evening));
        }
    }
}
