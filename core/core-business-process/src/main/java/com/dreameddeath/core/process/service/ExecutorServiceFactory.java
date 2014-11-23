package com.dreameddeath.core.process.service;

import com.dreameddeath.core.exception.process.ExecutorServiceNotFoundException;
import com.dreameddeath.core.exception.process.JobExecutionException;
import com.dreameddeath.core.exception.process.TaskExecutionException;
import com.dreameddeath.core.model.process.AbstractJob;
import com.dreameddeath.core.model.process.AbstractTask;
import com.dreameddeath.core.process.service.impl.BasicJobExecutorServiceImpl;
import com.dreameddeath.core.process.service.impl.BasicTaskExecutorServiceImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Christophe Jeunesse on 01/08/2014.
 */
public class ExecutorServiceFactory {
    private Map<Class<? extends AbstractJob>, IJobExecutorService<?>> _jobExecutorServicesMap
            = new ConcurrentHashMap<Class<? extends AbstractJob>, IJobExecutorService<?>>();
    private Map<Class<? extends AbstractTask>, ITaskExecutorService<?>> _taskExecutorServicesMap
            = new ConcurrentHashMap<Class<? extends AbstractTask>, ITaskExecutorService<?>>();


    public ExecutorServiceFactory(){
        addJobExecutorServiceFor(AbstractJob.class, new BasicJobExecutorServiceImpl());
        addTaskExecutorServiceFor(AbstractTask.class, new BasicTaskExecutorServiceImpl());
    }


    public <T extends AbstractTask> void addTaskExecutorServiceFor(Class<T> entityClass, ITaskExecutorService<T> service){
        _taskExecutorServicesMap.put(entityClass, service);
    }

    public <T extends AbstractTask> ITaskExecutorService<T> getTaskExecutorServiceForClass(Class<T> entityClass) {
        ITaskExecutorService<T> result = (ITaskExecutorService<T>) _taskExecutorServicesMap.get(entityClass);
        if (result == null) {
            Class parentClass = entityClass.getSuperclass();
            if (AbstractTask.class.isAssignableFrom(parentClass)) {
                result = getTaskExecutorServiceForClass(parentClass.asSubclass(AbstractTask.class));
                if (result != null) {
                    _taskExecutorServicesMap.put(entityClass, result);
                }
            }
        }
        ///TODO throw an error if null
        return result;
    }


    public <T extends AbstractJob> void addJobExecutorServiceFor(Class<T> entityClass, IJobExecutorService<T> service){
        _jobExecutorServicesMap.put(entityClass, service);
    }

    public <T extends AbstractJob> IJobExecutorService<T> getJobExecutorServiceForClass(Class<T> entityClass) throws ExecutorServiceNotFoundException {
        IJobExecutorService<T> result = (IJobExecutorService<T>) _jobExecutorServicesMap.get(entityClass);
        if (result == null) {
            Class parentClass = entityClass.getSuperclass();
            if (AbstractJob.class.isAssignableFrom(parentClass)) {
                result = getJobExecutorServiceForClass(parentClass.asSubclass(AbstractJob.class));
                if (result != null) {
                    _jobExecutorServicesMap.put(entityClass, result);
                }
            }
        }
        if(result==null){
            throw new ExecutorServiceNotFoundException("Cannot find execution class for job <"+entityClass.getName()+">");
        }
        ///TODO throw an error if null
        return result;
    }


    public <T extends AbstractJob> void execute(JobContext ctxt,T job) throws JobExecutionException,ExecutorServiceNotFoundException {
        ((IJobExecutorService<T>) getJobExecutorServiceForClass(job.getClass())).execute(ctxt,job);
    }

    public <T extends AbstractTask> void execute(TaskContext ctxt,T task) throws TaskExecutionException,ExecutorServiceNotFoundException {
        ((ITaskExecutorService<T>) getTaskExecutorServiceForClass(task.getClass())).execute(ctxt,task);
    }
}
