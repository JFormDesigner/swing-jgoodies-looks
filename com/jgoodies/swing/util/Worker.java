package com.jgoodies.swing.util;

import javax.swing.SwingUtilities;

/**
 * This is the 3rd version of SwingWorker (also known as SwingWorker 3), an
 * abstract class that you subclass to perform GUI-related work in a dedicated
 * thread.  For instructions on using this class, see
 * 
 * http://java.sun. com/docs/books/tutorial/uiswing/misc/threads.html
 * <p>
 * Note that the API changed slightly in the 3rd version: You must now
 * invoke start() on the SwingWorker after creating it.
 * <p>
 * JGoodies: Declared several methods as final.
 */
public abstract class Worker {
	
    private Object value; // see getValue(), setValue()

    /** 
     * Class to maintain reference to current worker thread
     * under separate synchronization control.
     */
    private static class ThreadVar {
        private Thread thread;
        ThreadVar(Thread t) 		{ thread = t; }
        synchronized Thread get()	{ return thread; }
        synchronized void clear()	{ thread = null; }
    }

    private ThreadVar threadVar;
    

    /**
     * Start a thread that will call the <code>construct</code> method
     * and then exit.
     */
    public Worker() {
        final Runnable doFinished = new Runnable() {
            public void run() {
                finished();
            }
        };
        Runnable doConstruct = new Runnable() {
            public void run() {
                try {
                    setValue(construct());
                } catch (Throwable t) {
                    t.printStackTrace();
                } finally {
                    threadVar.clear();
                }
                SwingUtilities.invokeLater(doFinished);
            }
        };
        Thread t = new Thread(doConstruct);
        threadVar = new ThreadVar(t);
    }
    
    
    /** 
     * Compute the value to be returned by the <code>get</code> method. 
     */
    public abstract Object construct();
    
    
    /**
     * Called on the event dispatching thread (not on the worker thread)
     * after the <code>construct</code> method has returned.
     */
    public void finished() {}
    
    
    /**
     * Return the value created by the <code>construct</code> method.  
     * Returns null if either the constructing thread or the current
     * thread was interrupted before a value was produced.
     * 
     * @return the value created by the <code>construct</code> method
     */
    public final Object get() {
        while (true) {
            Thread t = threadVar.get();
            if (t == null) {
                return getValue();
            }
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // propagate
                return null;
            }
        }
    }
    
    
    /** 
     * Get the value produced by the worker thread, or null if it 
     * hasn't been constructed yet.
     */
    protected synchronized final Object getValue() {
        return value;
    }
    
    
    /**
     * A new method that interrupts the worker thread.  Call this method
     * to force the worker to stop what it's doing.
     */
    public final void interrupt() {
        Thread t = threadVar.get();
        if (t != null) {
            t.interrupt();
        }
        threadVar.clear();
    }
    
    
    /** 
     * Set the value produced by worker thread 
     */
    private synchronized final void setValue(Object x) {
        value = x;
    }
    
    
    /**
     * Start the worker thread.
     */
    public final void start() {
        Thread t = threadVar.get();
        if (t != null) {
            t.start();
        }
    }
    
    
    /**
     * Start the worker thread.
     */
    public final void start(int priority) {
        Thread t = threadVar.get();
        if (t != null) {
            t.setPriority(priority);
            t.start();
        }
    }
}