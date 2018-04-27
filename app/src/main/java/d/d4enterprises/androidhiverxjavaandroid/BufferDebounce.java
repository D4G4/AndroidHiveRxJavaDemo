package d.d4enterprises.androidhiverxjavaandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

/**
 * The buffer gathers items emitted by an Observable into BATCHES and emit the batch instead of emitting one at a time.
 */
public class BufferDebounce extends AppCompatActivity {

  Disposable disposable;

  private int count;

  ArrayAdapter<String> adapter;
  private ArrayList<String> arrayList = new ArrayList<>();

  @BindView(R.id.list_item) ListView listView;

  @BindView(R.id.tap_result) TextView txtTapResult;

  @BindView(R.id.tap_result_max_count) TextView txtTapResultMax;

  @BindView(R.id.layout_tap_area) Button btnTapArea;

  private Unbinder unbinder;
  private int maxTaps = 0;

  private static final String TAG = MainActivity.class.getSimpleName();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_buffer_debounce);

    unbinder = ButterKnife.bind(this);

    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1,
        arrayList);

    listView.setAdapter(adapter);
    
    RxView.clicks(btnTapArea)
        .map(o -> 1)
        .buffer(5)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<Integer>>() {
          @Override public void onSubscribe(Disposable d) {
            disposable = d;
          }

          @Override public void onNext(List<Integer> integers) {
            addItem("On Next");
            if (integers.size() > 0) {
              maxTaps = integers.size() > maxTaps ? integers.size() : maxTaps;
              txtTapResult.setText(String.format("Received %d taps in 3 secs", integers.size()));
              txtTapResultMax.setText(
                  String.format("Maximum of %d taps received in this session", maxTaps));
            }
          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onComplete() {

          }
        });
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (disposable != null) disposable.dispose();
    if (unbinder != null) unbinder.unbind();
  }

  private void addItem(String text) {
    arrayList.add(text);
    if (adapter != null) adapter.notifyDataSetChanged();
    scrollToBottom();
  }

  public void subscribe(View view) {
    Observable<Integer> integerObservable = Observable.range(0, 10);

    integerObservable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .buffer(3)
        .subscribe(new Observer<List<Integer>>() {
          @Override public void onSubscribe(Disposable d) {

          }

          @Override public void onNext(List<Integer> integers) {
            addItem("onNext");
            for (Integer integer : integers) {
              addItem("Buffered " + integer);
            }
          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onComplete() {

          }
        });
  }

  private ArrayList<String> getArrayList() {
    String[] strings = {
        "Ant", "Bee", "Cow", "Dog", "Fox", "Lol", "Boxer", "Babool", "Quala"
    };

    ArrayList<String> arrayList = new ArrayList<>();
    for (int i = 0; i < strings.length; i++) {
      arrayList.add(strings[i]);
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    return arrayList;
  }

  public void showText(View view) {
    addItem("Clicked the button" + count++);
  }

  public void clearList(View view) {
    count = 0;
    arrayList.clear();
    adapter.notifyDataSetChanged();
  }

  private void scrollToBottom() {
    listView.smoothScrollToPosition(arrayList.size() - 1);
  }
}
