package uk.ac.brookes.alexrussell.pepperdashboard;

import android.arch.lifecycle.MutableLiveData;

import java.util.Map;

/**
 * Interface specifying the methods which any DataModel must provide to the ModelView and View.
 */
public interface DashboardDataModel {

    /**
     *
     * @return When the observable dataset is updated, the method returns the UpdateType. This serves as a trigger to update the
     * visualization.
     */
    MutableLiveData<UpdateType> getUpdateType();

    /**
     *
     * @return Returns the highest value for the CGM graph, so that the bounds of the chart can be set.
     */
    int getYUpperBound();

    /**
     *
     * @return Returns the lowest value for the CGM graph, so that the bounds of the chart can be set.
     */
    int getYLowerBound();

    /**
     *
     * @return Returns the upper blood glucose target.
     */
    int getUpperCgmTarget();

    /**
     *
     * @return Returns the lower blood glucose target.
     */
    int getLowerCgmTarget();

    /**
     *
     * @param hoursRange Time in hours.
     * @return Returns a map of CGM records for the selected time period.
     */
    Map<Long, Double> getCgmRecords (double hoursRange);

    /**
     *
     * @param hoursRange Time in hours.
     * @return Returns a map of bolus records for the selected time period.
     */
    Map<Long, Integer> getBolusRecords (double hoursRange);

    /**
     *
     * @param hoursRange Time in hours.
     * @return Returns a map of carbohydrates records for the selected time period.
     */
    Map<Long, Integer> getCarbsRecords (double hoursRange);

    /**
     *
     * @param hoursRange Time in hours.
     * @return Returns a map of activity records for the selected time period.
     */
    Map<Long, Integer> getActivityRecords (double hoursRange);

    /**
     *
     * @param time Time as long.
     * @return Returns CGM value for the timestamp.
     */
    double getCgmValue(long time);

    /**
     *
     * @return Returns the timestamp for the most recent record.
     */
    long getCurrentTime ();

    /**
     *
     * @return Returns the current carbohydrates value as a String.
     */
    String getCurrentCarbsValue();

    /**
     *
     * @return Returns the current bolus value as a String.
     */
    String getCurrentBolusValue();

    /**
     *
     * @return Returns the current CGM value as a String.
     */
    String getCurrentBloodGlucose();

    /**
     *
     * @return Returns the currrent trend.
     */
    Trend getCurrentTrend();
}
