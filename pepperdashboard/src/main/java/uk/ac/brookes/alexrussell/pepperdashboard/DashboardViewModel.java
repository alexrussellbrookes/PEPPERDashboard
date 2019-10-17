package uk.ac.brookes.alexrussell.pepperdashboard;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import uk.ac.brookes.alexrussell.chartinglibrary.ChartUtils;


/**
 * Example of a ViewModel within the PEPPER visualization architecture. Takes the raw data from the DataModel and models it for display by the
 * DashboardView.
 */
public class DashboardViewModel {
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private double timeRange;

    DashboardDataModel dataModel;

    public DashboardViewModel(DashboardDataModel dataModel) {
        this.dataModel = dataModel;
    }

    /**
     * Returns the time range (in hours) to be displayed.
     * @return
     */
    public double getTimeRange () {
        return timeRange;
    }

    /**
     * Sets the time range (in hours) to be displayed.
     * @param timeRange
     */
    public void setTimeRange (double timeRange) {
        this.timeRange = timeRange;
    }

    /**
     *
     * @param range Time in hours.
     * @return Returns a JSONArray of CGM records for the selected time period.
     */
    public JSONArray getCgmRecordSet(double range)  {
        return getLimitsRecordSet(range, dataModel.getCgmRecords(range) ,"level", "double");
    }

    /**
     *
     * @param range Time in hours.
     * @return Returns a JSONArray of carbohydrates records for the selected time period.
     */
    public JSONArray getCarbsRecordSet (double range) {
        return getLimitsRecordSet (range, dataModel.getCarbsRecords(range), "carbs", "int" );
    }

    /**
     *
     * @param range Time in hours.
     * @return Returns a JSONArray of bolus records for the selected time period.
     */
    public JSONArray getBolusRecordSet (double range) {
        return getLimitsRecordSet(range, dataModel.getBolusRecords(range), "bolus", "int");
    }

    /**
     * Tests whether the CGM data set can be animated.
     * @param timeRange
     * @return True if data set can be animated, else false.
     */
    public boolean isCgmAnimationSafe (double timeRange) {
        JSONArray cgmSet = getCgmRecordSet(timeRange);
        int setLength = cgmSet.length();
        if (setLength < 6) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param range Time in hours.
     * @return Returns a JSONArray of activity records for the selected time period.
     */
    public JSONArray getActivityRecordSet (double range) {
        return getLimitsRecordSet (range, dataModel.getActivityRecords(range), "activity", "int");
    }

    private JSONArray getLimitsRecordSet (double range, Map recordSet, String unit, String type) {
        TreeMap<Long, Double> setWithLimitValues = new TreeMap<>();
        if (recordSet != null) {
            setWithLimitValues.putAll(recordSet);
        }
        long start = dataModel.getCurrentTime() - (long)(range * HOUR);
        if (!setWithLimitValues.containsKey(dataModel.getCurrentTime())) {
            setWithLimitValues.put(dataModel.getCurrentTime(), null);
        }
        if (!setWithLimitValues.containsKey(start)) {
            setWithLimitValues.put(start, null);
        }
        return getRecordSet(setWithLimitValues, unit, type);
    }

    private JSONArray getRecordSet(Map map, String unit, String type) {
        JSONArray jArray = new JSONArray();
        Set<Long> keys = map.keySet();
        Iterator<Long> keyIter = keys.iterator();
        while (keyIter.hasNext()) {
            double value = 0;
            long key = keyIter.next();
            if (map.get(key)!=null) {
                if (type.equals("int")) {
                    int intValue = (int) map.get(key);
                    value = (double) intValue;
                } else {
                    value = (double) map.get(key);
                }
            }
            try {
                JSONObject jObj = ChartUtils.getJSONDate("date", key);
                BigDecimal bd = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
                if (map.get(key)!=null) {
                    jObj.put(unit, bd);
                }
                else {
                    jObj.put(unit, null);
                }
                jArray.put(jObj);
            } catch (Exception e) {
                Log.v("tag", e.getMessage());
            }
        }
        return jArray;
    }

    /**
     * Returns the maximum value to be displayed on the Y Axis of the CGM chart.
     * @return
     */
    public int getYUpperBound() {
        return dataModel.getYUpperBound();
    }

    /**
     * Returns the minimum value to be displayed on the Y Axis of the CGM chart.
     * @return
     */
    public int getYLowerBound() {
        return dataModel.getYLowerBound();
    }

    /**
     * Returns the lower value for the target line on the CGM chart.
     * @return
     */
    public int getLowerCgmTarget(){
        return dataModel.getLowerCgmTarget();
    }

    /**
     * Returns the upper value for the target line on the CGM chart.
     * @return
     */
    public int getUpperCgmTarget(){
        return dataModel.getUpperCgmTarget();
    }

    /**
     * Gets the trend of the CGM results and returns a string with the file name of the arrow to be displayed.
     * @return
     */
    public String getCurrentTrend () {
        Trend trend = dataModel.getCurrentTrend();
        String trendString = "";
        if (trend==Trend.STEADY) {
            trendString = "./cgm_steady.png";
        }
        else if (trend==Trend.SLOW_RISE){
            trendString = "./cgm_slow_rise.png";
        }
        else if (trend==Trend.RISE){
            trendString = "./cgm_rise.png";
        }
        else if (trend==Trend.RAPID_RISE){
            trendString = "./cgm_rapid_rise.png";
        }
        else if (trend==Trend.SLOW_FALL){
            trendString = "./cgm_slow_fall.png";
        }
        else if (trend==Trend.FALL){
            trendString = "./cgm_fall.png";
        }
        else if (trend==Trend.RAPID_FALL){
            trendString = "./cgm_rapid_fall.png";
        }
        return trendString;
    }

    /**
     * Returns the current value for carbohydrates level.
     * @return
     */
    public String getCurrentCarbsValue() {
        return dataModel.getCurrentCarbsValue();
    }

    /**
     * Returns the current value for the bolus insulin level.
     * @return
     */
    public String getCurrentBolusValue() {
        return dataModel.getCurrentBolusValue();
    }

    /**
     * Returns the current value for the blood glucose level.
     * @return
     */
    public String getCurrentBloodGlucose() {return dataModel.getCurrentBloodGlucose();}


}
