# Works Base Android
===

Base library for new upcoming library for Works libraries for Android as well as base for building Android projects.

# Features
===

## Works Controller
Works Controller is a class that you can utilise for 

* Object persistence for Fragment, especially nested Fragments where setRetainInstance no longer works.
* Controlling background execution flow during device rotation
* And many other things

The basic of Works Controller, derived from LoaderManager from Android SDK with added features. LoaderManager's Loader and loaded objects are maintained across device rotation even for nested Fragments. However Loader concepts lack of Android lifecycle monitoring (onStart, onResume and so on) whereas Controller does.

Usage pattern can be seen from usage of the implementation of WorksController named BoltsWorkController.

```java
public class BoltsActivityFragment extends WorksFragment
{
    private BoltsWorkController mBwc;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        WorksControllerManager manager = getControllerManager();

        mBwc = manager.initController(0, null, new BoltsWorkController.ControllerCallbacks());
        mBwc.setContinuation("login", new BoltsWorkController.ContinuationFactory<Boolean>()
        {
            @Override
            public Task<Boolean> continueWith(Task<Boolean> task)
            {
                DialogFragment dialog = (DialogFragment) getFragmentManager().findFragmentByTag("dialog");
                if (dialog != null)
                {
                    dialog.dismiss();
                }

                return null;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void onLogin(View view)
    {
        Task<Boolean> task;

        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        dialogFragment.show(getFragmentManager(), "dialog");

        task = Task.call(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                return doLogin();
            }
        }).continueWith(new Continuation<Boolean, Boolean>()
        {
            @Override
            public Boolean then(Task<Boolean> task) throws Exception
            {
                return task.getResult();
            }
        }, WorksExecutor.MAIN);
        mBwc.addTask("login", task);
    }
}
```

As can be seen in the usage above, usually we put a dialog as indicator of background operation. On conventional coding, you might want to move the login operation to Service level or make it as static (aw). Then you need to put indicator on savedInstanceState in order to search for dialog, and so on .. which even explaining it is complicated.

With WorksController, since it maintain the persistence within, it can be used to store the background operation, request and task state, and what to do when the task is done. 

The question might arise in which, why the continuation had to be re-initialised the inner class, every time during onCreate rather than within the onLogin flow. The reason being is that we need to always re-initialise function related to the Fragment, as the Fragment was destroyed during transition, and previous inner class will still bound the the previous Fragment instance.
