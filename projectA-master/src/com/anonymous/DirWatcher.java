package com.anonymous;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * Created by Adam on 10.04.2018.
 */
public class DirWatcher implements Runnable{

    private Thread t;
    private String threadName = "Inuidisse Thread 1";
    private String path;

    DirWatcher(String path)
    {
        this.path = path;
    }

    @Override
    public void run() {
        watch();
    }

    void watch()
    {
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path dir = Paths.get(path);
            dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

            System.out.println("Watch Service registered for dir: " + dir.getFileName());

            while (true) {
                WatchKey key;
                try {
                    key = watcher.take();
                } catch (InterruptedException ex) {
                    return;
                }

                try {
                    Thread.sleep(2000);
                }
                catch (InterruptedException e)
                {
                    System.err.println("Thread sleep error");
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    System.out.println("Hi");
                    //Thread.sleep(1000);

                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();

                    System.out.println(kind.name() + ": " + fileName);

                    if (kind == ENTRY_MODIFY &&
                            fileName.toString().equals("DirectoryWatchDemo.java")) {
                        System.out.println("My source file has changed!!!");
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }

            }

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void start () {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
}
