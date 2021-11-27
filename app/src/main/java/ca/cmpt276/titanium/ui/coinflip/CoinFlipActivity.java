package ca.cmpt276.titanium.ui.coinflip;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;
import java.util.UUID;

import ca.cmpt276.titanium.R;
import ca.cmpt276.titanium.model.Child;
import ca.cmpt276.titanium.model.ChildManager;
import ca.cmpt276.titanium.model.ChildQueueManager;
import ca.cmpt276.titanium.model.Coin;
import ca.cmpt276.titanium.model.CoinFlip;
import ca.cmpt276.titanium.model.CoinFlipManager;

/**
 * This activity represents the coin flip activity.
 * Allows the user to choose heads or tails, and shows the results of the flip.
 */
public class CoinFlipActivity extends AppCompatActivity {
  private static final int COIN_FLIP_DELAY = 1600;
  private static final Coin DEFAULT_COIN = Coin.HEADS;

  private Coin coinChosen;
  private CoinFlipManager coinFlipManager;
  private ChildQueueManager childQueueManager;

  public static Intent makeIntent(Context context) {
    return new Intent(context, CoinFlipActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_coin_flip);
    setTitle(R.string.title_flip_coin);

    Toolbar myToolbar = (Toolbar) findViewById(R.id.ToolBar_coin_flip);
    setSupportActionBar(myToolbar);
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    coinFlipManager = CoinFlipManager.getInstance(this);
    childQueueManager = ChildQueueManager.getInstance(this);

    setupButtons();
    updateGUI(DEFAULT_COIN);
  }

  @Override
  protected void onResume() {
    super.onResume();
    updateGUI(coinChosen);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_coin_flip, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    } else if (item.getItemId() == R.id.menu_item_coin_flip_coin_flip_history) {
      startActivity(new Intent(this, CoinFlipHistoryActivity.class));
      return true;
    } else if (item.getItemId() == R.id.menu_item_coin_flip_coin_flip_child_queue) {
      startActivity(new Intent(this, ChildQueueActivity.class));
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  private void setupButtons() {
    Button headsButton = findViewById(R.id.Button_coin_flip_choose_heads);
    Button tailsButton = findViewById(R.id.Button_coin_flip_choose_tails);
    Button flipButton = findViewById(R.id.Button_coin_flip_flip);

    headsButton.setOnClickListener((View view) -> updateGUI(Coin.HEADS));
    tailsButton.setOnClickListener((View view) -> updateGUI(Coin.TAILS));
    flipButton.setOnClickListener(view -> animateCoinFlip());
  }

  private void updateGUI(Coin coinChosen) {
    ImageView childIconDisplay = findViewById(R.id.ImageView_coin_flip_child_portrait);
    TextView childNameDisplay = findViewById(R.id.TextView_coin_flip_child_name);
    TextView sideChosenDisplay = findViewById(R.id.TextView_coin_flip_coin_side_chosen);
    Button headsButton = findViewById(R.id.Button_coin_flip_choose_heads);
    Button tailsButton = findViewById(R.id.Button_coin_flip_choose_tails);

    UUID nextPickerUniqueID = coinFlipManager.getNextPickerUniqueID();

    if (nextPickerUniqueID != null) {
      ChildManager childManager = ChildManager.getInstance(this);
      Child nextChild = childManager.getChild(nextPickerUniqueID);
      childIconDisplay.setImageDrawable(nextChild.getPortrait(getResources()));
      childNameDisplay.setText(getString(R.string.coin_flip_child_name, nextChild.getName()));
      sideChosenDisplay.setText(getString(R.string.coin_flip_side_chosen, coinChosen.toString()));
      this.coinChosen = coinChosen;
    } else {
      childNameDisplay.setVisibility(View.INVISIBLE);
      sideChosenDisplay.setVisibility(View.INVISIBLE);
      headsButton.setVisibility(View.INVISIBLE);
      tailsButton.setVisibility(View.INVISIBLE);
    }
  }

  private void animateCoinFlip() {
    ImageView coin = findViewById(R.id.ImageView_coin_flip_coin);
    coin.setImageResource(R.drawable.ic_coin_blank_yellow_200);

    TextView coinResultMessage = findViewById(R.id.TextView_coin_flip_result);
    coinResultMessage.setVisibility(View.INVISIBLE);

    coin.startAnimation(AnimationUtils.loadAnimation(this, R.anim.coin_flip));
    MediaPlayer.create(CoinFlipActivity.this, R.raw.sound_coin_flip).start();

    Coin coinResult = CoinFlip.flipCoin();

    if (coinResult.equals(Coin.HEADS)) {
      coinResultMessage.setText(R.string.coin_flip_result_heads);
      coin.postDelayed(() ->
          coin.setImageResource(R.drawable.ic_coin_heads_yellow_200), COIN_FLIP_DELAY);
    } else {
      coinResultMessage.setText(R.string.coin_flip_result_tails);
      coin.postDelayed(() ->
          coin.setImageResource(R.drawable.ic_coin_tails_yellow_200), COIN_FLIP_DELAY);
    }

    coinResultMessage.postDelayed(() ->
        coinResultMessage.setVisibility(View.VISIBLE), COIN_FLIP_DELAY);

    if (coinFlipManager.getNextPickerUniqueID() != null) {
      childQueueManager.moveToBack(coinFlipManager.getNextPickerUniqueID());
      coinFlipManager.addCoinFlip(coinChosen, coinResult);
      new Handler(Looper.getMainLooper()).postDelayed(() -> updateGUI(coinChosen), COIN_FLIP_DELAY);
    }
  }
}
