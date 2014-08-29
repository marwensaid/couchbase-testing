package com.dreameddeath.core.process;

import com.dreameddeath.core.model.process.AbstractJob;
import com.dreameddeath.core.model.process.AbstractTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Christophe Jeunesse on 01/08/2014.
 */
public class ProcessingServiceFactory {
    private Map<Class<? extends AbstractJob>, JobProcessingService<?>> _jobServicesMap
            = new HashMap<Class<? extends AbstractJob>, JobProcessingService<?>>();


    private Map<Class<? extends AbstractTask>, TaskProcessingService<?>> _taskServicesMap
            = new HashMap<Class<? extends AbstractTask>, TaskProcessingService<?>>();


    public ProcessingServiceFactory(){
        addJobServiceFor(AbstractJob.class, new BasicJobProcessingServiceImpl(this));
        addTaskServiceFor(AbstractTask.class, new BasicTaskProcessingServiceImpl(this));
    }


    public <T extends AbstractTask> void addTaskServiceFor(Class<T> entityClass,TaskProcessingService<T> service){
        _taskServicesMap.put(entityClass, service);
    }

    public <T extends AbstractTask> TaskProcessingService<T> getTaskServiceForClass(Class<T> entityClass) {
        TaskProcessingService<T> result = (TaskProcessingService<T>) _taskServicesMap.get(entityClass);
        if (result == null) {
            Class parentClass = entityClass.getSuperclass();
            if (AbstractTask.class.isAssignableFrom(parentClass)) {
                result = getTaskServiceForClass(parentClass.asSubclass(AbstractTask.class));
                if (result != null) {
                    _taskServicesMap.put(entityClass, result);
                }
            }
        }
        ///TODO throw an error if null
        return result;
    }


    public <T extends AbstractJob> void addJobServiceFor(Class<T> entityClass,JobProcessingService<T> service){
        _jobServicesMap.put(entityClass,service);
    }

    public <T extends AbstractJob> JobProcessingService<T> getJobServiceForClass(Class<T> entityClass) {
        JobProcessingService<T> result = (JobProcessingService<T>) _jobServicesMap.get(entityClass);
        if (result == null) {
            Class parentClass = entityClass.getSuperclass();
            if (AbstractJob.class.isAssignableFrom(parentClass)) {
                result = getJobServiceForClass(parentClass.asSubclass(AbstractJob.class));
                if (result != null) {
                    _jobServicesMap.put(entityClass, result);
                }
            }
        }
        ///TODO throw an error if null
        return result;
    }
}