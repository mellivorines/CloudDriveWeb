package io.github.mellivorines.clouddriveweb.schedule;

/**
 * 自定义业务逻辑执行器接口
 */
public interface RPanScheduleTask extends Runnable {

    /**
     * 获取执行器名称
     *
     * @return
     */
    String getName();

}
