package adris.altoclef.tasksystem.chains;

import adris.altoclef.AltoClef;
import adris.altoclef.Debug;
import adris.altoclef.tasksystem.Task;
import adris.altoclef.tasksystem.TaskChain;
import adris.altoclef.tasksystem.TaskRunner;
import adris.altoclef.util.csharpisbetter.Stopwatch;

public abstract class SingleTaskChain extends TaskChain {

    protected Task _mainTask = null;
    private final Stopwatch _taskStopwatch = new Stopwatch();

    private boolean _interrupted = false;

    public SingleTaskChain(TaskRunner runner) {
        super(runner);
    }

    @Override
    protected void onTick(AltoClef mod) {
        if (!isActive()) return;

        if (_interrupted) {
            _interrupted = false;
            if (_mainTask != null) {
                _mainTask.reset();
            }
        }

        if (_mainTask != null) {
            if ((_mainTask.isFinished(mod)) || _mainTask.stopped()) {
                onTaskFinish(mod);
            } else {
                _mainTask.tick(mod, this);
            }
        }
    }

    protected void onStop(AltoClef mod) {
        if (isActive() && _mainTask != null) {
            _mainTask.stop(mod);
            _mainTask = null;
        }
    }

    public void setTask(Task task) {
        if (_mainTask == null || !_mainTask.equals(task)) {
            _mainTask = task;
            if (task != null) task.reset();
        }
    }


    @Override
    public boolean isActive() {
        return _mainTask != null;
    }

    protected abstract void onTaskFinish(AltoClef mod);

    @Override
    public void onInterrupt(AltoClef mod, TaskChain other) {
        Debug.logInternal("Chain Interrupted: " + this.toString() + " by " + other.toString());
        // Stop our task. When we're started up again, let our task know we need to run.
        _interrupted = true;
        if (_mainTask != null && _mainTask.isActive()) {
            _mainTask.stop(mod);
        }
    }

    public Task getCurrentTask() {
        return _mainTask;
    }
}
