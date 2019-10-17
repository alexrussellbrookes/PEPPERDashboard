package uk.ac.brookes.alexrussell.pepperdashboard;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.WebView;

import org.json.JSONArray;

import java.math.BigDecimal;
import java.math.RoundingMode;

import uk.ac.brookes.alexrussell.chartinglibrary.AnimationType;
import uk.ac.brookes.alexrussell.chartinglibrary.ChartHolder;
import uk.ac.brookes.alexrussell.chartinglibrary.ChartModelBi;
import uk.ac.brookes.alexrussell.chartinglibrary.ChartUpdate;
import uk.ac.brookes.alexrussell.chartinglibrary.ChartViewClient;
import uk.ac.brookes.alexrussell.chartinglibrary.ChartViewHandler;
import uk.ac.brookes.alexrussell.chartinglibrary.CurveType;
import uk.ac.brookes.alexrussell.chartinglibrary.LineChart;
import uk.ac.brookes.alexrussell.chartinglibrary.PepperBubbleChart;


/**
 * Example of the PEPPER dashboard View. Prepares the charts which are to be displayed and specifies how they are to be updated.
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class DashboardView {

    DashboardViewModel viewModel;
    WebView webView;
    Activity activity;
    DashboardDataModel dbDataModel;
    ChartHolder chartHolder;
    ChartViewClient client;
    ChartViewHandler handler;

    /**
     *
     * @param activity The Activity to which the DashboardView belongs.
     * @param webView The WebView in which the visualization will be displayed.
     * @param dbDataModel The data model that will supply data to the ViewModel.
     */
    public DashboardView(Activity activity, WebView webView, DashboardDataModel dbDataModel) {
        this.activity = activity;
        this.webView = webView;
        this.dbDataModel = dbDataModel;
        viewModel = new DashboardViewModel(dbDataModel);
        viewModel.setTimeRange(12);


        String currentViewSpec = "d3.select(\"body\").append(\"div\")\n" +
                "                .attr(\"id\", \"currentValues\");\n" +
                "\n" +
                "var svg = d3.select(\"#currentValues\").append(\"svg\");\n" +
                "svg.attr(\"width\", \"960\").attr(\"height\", \"350\");\n" +
                "\n" +
                "var defs = svg.append(\"defs\");\n" +
                "var filter = defs.append(\"filter\")\n" +
                "                .attr(\"id\", \"shadow\");\n" +
                "filter.append(\"feDropShadow\")\n" +
                "      .attr(\"dx\", \"4\")\n" +
                "      .attr(\"dy\", \"4\")\n" +
                "      .attr(\"stdDeviation\", \"4\");\n" +
                "var bgLevel = svg.append(\"g\").attr(\"id\", \"bglevel\")\n" +
                "                            .attr(\"transform\", \"translate(650,150)\");\n" +
                "bgLevel.append(\"circle\").attr(\"id\", \"bglevelcircle\")\n" +
                "                        .attr(\"cx\", \"0\")\n" +
                "                        .attr(\"cy\", \"0\")\n" +
                "                        .attr(\"r\", \"100\")\n" +
                "                        .attr(\"fill\", \"#FF00FF\")\n" +
                "                        .attr(\"style\", \"filter:url(#shadow)\");\n" +
                "bgLevel.append(\"text\").attr(\"id\", \"bgleveltext\")\n" +
                "                        .attr(\"x\", \"0\")\n" +
                "                        .attr(\"y\", \"0\")\n" +
                "                        .attr(\"text-anchor\", \"middle\")\n" +
                "                        .attr(\"font-size\", \"40\");\n" +
                "bgLevel.append(\"text\").attr(\"id\", \"bglevellabel\")\n" +
                "                        .attr(\"x\", \"0\")\n" +
                "                        .attr(\"y\", \"0\")\n" +
                "                        .attr(\"dx\", \"-30\")\n" +
                "                        .attr(\"dy\", \"130\")\n" +
                "                      .attr(\"font-size\", \"25\")\n" +
                "                      .attr(\"fill\", \"grey\")\n" +
                "                      .html(\"mmol/l\");\n" +
                "\n" +
                "var carbGroup = svg.append(\"g\").attr(\"id\", \"carbgroup\").attr(\"transform\", \"translate(390,150)\");\n" +
                "carbGroup.append(\"circle\").attr(\"id\", \"carbcircle\")\n" +
                "                        .attr(\"cx\", \"0\")\n" +
                "                        .attr(\"cy\", \"0\")\n" +
                "                        .attr(\"r\", \"100\")\n" +
                "                        .attr(\"fill\", \"yellow\")\n" +
                "                        .attr(\"style\", \"filter:url(#shadow)\");\n" +
                "carbGroup.append(\"text\").attr(\"id\", \"carbtext\")\n" +
                "                        .attr(\"x\", \"0\")\n" +
                "                        .attr(\"y\", \"0\")\n" +
                "                        .attr(\"text-anchor\", \"middle\")\n" +
                "                        .attr(\"font-size\", \"40\");\n" +
                "carbGroup.append(\"text\").attr(\"x\", \"0\")\n" +
                "                        .attr(\"y\", \"0\")\n" +
                "                        .attr(\"text-anchor\", \"middle\")\n" +
                "                        .attr(\"dy\", \"130\")\n" +
                "                      .attr(\"font-size\", \"25\")\n" +
                "                      .attr(\"fill\", \"grey\")\n" +
                "                      .html(\"CoB\");\n" +
                "\n" +
                "var bolusGroup = svg.append(\"g\").attr(\"id\", \"bolusgroup\").attr(\"transform\", \"translate(50,65)\");\n" +
                "bolusGroup.append(\"rect\").attr(\"id\", \"bolusrect\")\n" +
                "                        .attr(\"x\", \"0\")\n" +
                "                        .attr(\"y\", \"0\")\n" +
                "                        .attr(\"width\", \"175\")\n" +
                "                        .attr(\"height\", \"175\")\n" +
                "                        .attr(\"fill\", \"#BFF0FE\")\n" +
                "                        .attr(\"style\", \"filter:url(#shadow)\");\n" +
                "bolusGroup.append(\"text\").attr(\"id\", \"bolustext\")\n" +
                "                        .attr(\"x\", \"60\")\n" +
                "                        .attr(\"y\", \"80\")\n" +
                "                        .attr(\"font-size\", \"40\");\n" +
                "bolusGroup.append(\"text\").attr(\"x\", \"0\")\n" +
                "                        .attr(\"y\", \"0\")\n" +
                "                        .attr(\"dx\", \"60\")\n" +
                "                        .attr(\"dy\", \"215\")\n" +
                "                      .attr(\"font-size\", \"25\")\n" +
                "                      .attr(\"fill\", \"grey\")\n" +
                "                      .html(\"BoB\");\n" +
                "var trend = svg.append(\"g\").attr(\"id\", \"bgtrend\").attr(\"transform\", \"translate(800,100)\");\n" +
                "trend.append(\"image\").attr(\"id\", \"trendImage\")\n" +
                "                     .attr(\"x\", \"0\")\n" +
                "                     .attr(\"y\", \"0\")\n" +
                "                     .attr(\"width\", \"100\")\n" +
                "                     .attr(\"height\", \"100\")\n" +
                "                     .attr(\"xlink:href\", \"\");\n" +
                "\n";



        chartHolder = new ChartHolder(activity);

        LineChart lineChart = new LineChart(new ChartModelBi(), viewModel.getCgmRecordSet(viewModel.getTimeRange()), "date", "level", true,true);
        lineChart.setChartID("cgm");
        lineChart.setHeight(700);
        lineChart.setWidth(960);
        lineChart.setMarginTop(70);
        lineChart.setMarginRight(70);
        lineChart.setMarginLeft(70);
        lineChart.setFontSize(28);
        lineChart.setYDomainSpec(new double[] {viewModel.getYLowerBound(), viewModel.getYUpperBound()});
        lineChart.setTargetLines(new double[] {viewModel.getLowerCgmTarget(), viewModel.getUpperCgmTarget()});
        lineChart.setDataPoints(true);
        lineChart.setZoom(true);
        lineChart.setYAxisLabel("mg/dL");
        lineChart.setXAxisLabel("Time");
        lineChart.setCurveType(CurveType.CURVE_MONOTONE_X);
        lineChart.setTooltipLabelX("Time");
        lineChart.setTooltipLabelY("BG Level");
        chartHolder.addChart(lineChart);

        PepperBubbleChart bolusChart = new PepperBubbleChart(new ChartModelBi(), viewModel.getBolusRecordSet(12), "date", "bolus", "bolus", true,true);
        bolusChart.setChartID("bolus");
        bolusChart.setBubbleLabel("bolus");
        bolusChart.setHeight(350);
        bolusChart.setMarginTop(50);
        bolusChart.setMarginRight(110);
        bolusChart.setMarginLeft(70);
        bolusChart.setFontSize(28);
        bolusChart.setBubbleColor("#BFF0FE");
        bolusChart.setYAxisLabel("Bolus IU");
        bolusChart.setUnitText("IU");
        bolusChart.setBubbleProp(false);
        bolusChart.setYAligned(false);
        bolusChart.setYTicks(false);
        bolusChart.setTextFill(true);
        bolusChart.setSideBar(true);
        chartHolder.addChart(bolusChart);

        PepperBubbleChart carbsChart = new PepperBubbleChart(new ChartModelBi(), viewModel.getCarbsRecordSet(12), "date", "carbs", "carbs", true, true);
        carbsChart.setChartID("carbs");
        carbsChart.setBubbleLabel("carbs");
        carbsChart.setHeight(350);
        carbsChart.setMarginTop(50);
        carbsChart.setMarginRight(110);
        carbsChart.setMarginLeft(70);
        carbsChart.setFontSize(28);
        carbsChart.setBubbleColor("#f4f142");
        carbsChart.setYAxisLabel("Carbs g");
        carbsChart.setUnitText("g");
        carbsChart.setBubbleProp(false);
        carbsChart.setYAligned(false);
        carbsChart.setYTicks(false);
        carbsChart.setTextFill(true);
        carbsChart.setSideBar(true);
        chartHolder.addChart(carbsChart);

        PepperBubbleChart activityChart = new PepperBubbleChart(new ChartModelBi(), viewModel.getActivityRecordSet(12), "date", "activity", "activity", true, true);
        activityChart.setChartID("activity");
        activityChart.setBubbleLabel("activity");
        activityChart.setHeight(350);
        activityChart.setMarginTop(50);
        activityChart.setMarginRight(110);
        activityChart.setMarginLeft(70);
        activityChart.setFontSize(28);
        activityChart.setBubbleColor("#f4f142");
        activityChart.setYAxisLabel("Activity");
        activityChart.setExtraText("Est");
        activityChart.setSumText("HbA1c");
        activityChart.setUnitText("0%");
        activityChart.setBubbleProp(false);
        activityChart.setYAligned(true);
        activityChart.setYTicks(false);
        activityChart.setTextFill(true);
        activityChart.setSideBar(true);
        activityChart.setRectDisplay(true);
        chartHolder.addChart(activityChart);


        client = new ChartViewClient (chartHolder);
        client.addJS(currentViewSpec);

        BigDecimal bgDec = new BigDecimal(viewModel.getCurrentBloodGlucose()).setScale(2, RoundingMode.HALF_UP);
        client.addJS("d3.select(\"#bgleveltext\").html(" + bgDec + ");");

        BigDecimal bolusDec = new BigDecimal(viewModel.getCurrentBolusValue()).setScale(2, RoundingMode.HALF_UP);
        client.addJS("d3.select(\"#bolustext\").html(" + bolusDec + ");");

        BigDecimal carbsDec = new BigDecimal(viewModel.getCurrentCarbsValue()).setScale(2, RoundingMode.HALF_UP);
        client.addJS("d3.select(\"#carbtext\").html(" + carbsDec + ");");

        client.addJS("d3.select(\"#trendImage\").attr(\"xlink:href\", \"" + viewModel.getCurrentTrend() + "\");");

        handler = new ChartViewHandler(webView, chartHolder, client);
        handler.loadCharts();
    }

    /**
     * Sets the time range to be displayed, as chosen by the user.
     * @param selectedNumber The time range (in hours) selected.
     */
    public void setTimeRange (double selectedNumber) {
        viewModel.setTimeRange(selectedNumber);
        generalUpdate(false);
    }

    /**
     * Updates all the components on the dashboard.
     * @param shiftOne True if the data has been increased by one data new data point, else false.
     */
    public void generalUpdate(boolean shiftOne) {
        updateCurrentCgm();
        updateCurrentBolus();
        updateCurrentCarbs();
        updateCurrentTrend();
        updateCgm(shiftOne);
        updateBolus();
        updateCarbs();
        updateActivity();
    }

    /**
     * Updates the CGM value at the top of the display.
     */
    public void updateCurrentCgm () {
        BigDecimal bgDec = new BigDecimal(viewModel.getCurrentBloodGlucose()).setScale(2, RoundingMode.HALF_UP);
        webView.evaluateJavascript("d3.select(\"#bgleveltext\").html(" + bgDec + ");", null);
    }

    /**
     * Updates the bolus value at the top of the display.
     */
    public void updateCurrentBolus() {
        BigDecimal bolusDec = new BigDecimal(viewModel.getCurrentBolusValue()).setScale(2, RoundingMode.HALF_UP);
        webView.evaluateJavascript("d3.select(\"#bolustext\").html(" + bolusDec + ");", null);
    }

    /**
     * Updates the carbohydrates value at the top of the display.
     */
    public void updateCurrentCarbs(){
        BigDecimal carbsDec = new BigDecimal(viewModel.getCurrentCarbsValue()).setScale(2, RoundingMode.HALF_UP);
        webView.evaluateJavascript("d3.select(\"#carbtext\").html(" + carbsDec + ");", null);
    }

    /**
     * Updates the trend arrow at the top of the display.
     */
    public void updateCurrentTrend(){
        webView.evaluateJavascript("d3.select(\"#trendImage\").attr(\"xlink:href\", \"" + viewModel.getCurrentTrend() + "\");", null);
    }

    /**
     * Triggered by the update button on the display. Creates a wheel displaying the number of hours to be displayed.
     */
    public void takeUpdateCall () {
        DialogFragment dialog = new NoticeDialogFragment();
        dialog.show(activity.getFragmentManager(), "NoticeDialogFragment");
    }

    /**
     * Updates the CGM line graph.
     * @param shiftOne True if the data has been increased by one data new data point, else false.
     */
    public void updateCgm(boolean shiftOne) {
        ChartUpdate chartUpdate = chartHolder.getChartUpdate("cgm");
        JSONArray dataString = viewModel.getCgmRecordSet(viewModel.getTimeRange());

        if(!shiftOne){
           chartUpdate.setAnimationType(AnimationType.FADE);
        }
        else {
            chartUpdate.setAnimationType(AnimationType.SLIDE);
        }
        if (!viewModel.isCgmAnimationSafe(viewModel.getTimeRange())) {
            chartUpdate.setAnimationType(AnimationType.GENERAL);
        }
        chartUpdate.setUpdateString(dataString.toString());
        handler.loadUpdate(chartUpdate);
    }

    /**
     * Updates the bolus bubble graph.
     */
    private void updateBolus () {
        ChartUpdate chartUpdate = chartHolder.getChartUpdate("bolus");
        chartUpdate.setUpdateString(viewModel.getBolusRecordSet(viewModel.getTimeRange()).toString());
        handler.loadUpdate(chartUpdate);
    }

    /**
     * Updates the carbohydrates bubble graph.
     */
    private void updateCarbs () {
        ChartUpdate chartUpdate = chartHolder.getChartUpdate("carbs");
        chartUpdate.setUpdateString(viewModel.getCarbsRecordSet(viewModel.getTimeRange()).toString());
        handler.loadUpdate(chartUpdate);
    }

    /**
     * Updates the activity bubble graph.
     */
    private void updateActivity() {
        ChartUpdate chartUpdate = chartHolder.getChartUpdate("activity");
        chartUpdate.setUpdateString(viewModel.getActivityRecordSet(viewModel.getTimeRange()).toString());
        handler.loadUpdate(chartUpdate);
    }
}
