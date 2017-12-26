package protect.gift_card_guard;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 17)
public class GiftCardCursorAdapterTest
{
    @Test
    public void TestCursorAdapter()
    {
        ActivityController activityController = Robolectric.buildActivity(MainActivity.class).create();
        Activity activity = (Activity)activityController.get();

        DBHelper db = new DBHelper(activity);
        db.insertGiftCard("store", "cardId", "value", "receipt");
        GiftCard card = db.getGiftCard(1);

        Cursor cursor = db.getGiftCardCursor();
        cursor.moveToFirst();

        final String CURRENCY = "MadeUpCurrency";
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(
                activity);
        preferences.edit().putString("currency", CURRENCY).apply();

        GiftCardCursorAdapter adapter = new GiftCardCursorAdapter(activity.getApplicationContext(), cursor);

        View view = adapter.newView(activity.getApplicationContext(), cursor, null);
        adapter.bindView(view, activity.getApplicationContext(), cursor);

        final TextView storeField = (TextView) view.findViewById(R.id.store);
        final TextView valueField = (TextView) view.findViewById(R.id.value);

        assertEquals(card.store, storeField.getText().toString());
        assertEquals(card.value, valueField.getText().toString());

        final TextView cardIdField = (TextView) view.findViewById(R.id.cardId);
        final String cardIdLabel = activity.getResources().getString(R.string.cardId);
        final String cardIdFormat = activity.getResources().getString(R.string.cardIdFormat);
        String cardIdText = String.format(cardIdFormat, cardIdLabel, "cardId");
        assertEquals(cardIdText, cardIdField.getText().toString());

        final TextView currencyField = (TextView) view.findViewById(R.id.currency);
        assertEquals(CURRENCY, currencyField.getText().toString());
    }
}
