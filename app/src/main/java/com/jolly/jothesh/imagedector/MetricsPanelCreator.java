package com.jolly.jothesh.imagedector;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MetricsPanelCreator {

    static TextView[] createScoresTextViews() {
        return new TextView[MetricsManager.getTotalNumMetrics()];
    }

    static LinearLayout populateMetricsContainer(LinearLayout container, TextView[] scores, Context context) {

        //Populate Emotions
        container.addView(createHeaderTextView("EMOTIONS", context));
        int[] emotionCodes = MetricsManager.getEmotionsIndexArray();
        for (int eachCode: emotionCodes) {
            container.addView(createNameTextView(eachCode, context));
            container.addView(createScoreTextView(eachCode, scores, context));
        }

        //Populate Measurements
        container.addView(createHeaderTextView("MEASUREMENTS", context));
        int[] measurementCodes = MetricsManager.getMeasurementsIndexArray();
        for (int eachCode: measurementCodes) {
            container.addView(createNameTextView(eachCode,context));
            container.addView(createScoreTextView(eachCode,scores,context));
        }

        //Populate Appearence
        container.addView(createHeaderTextView("APPEARANCE",context));
        int[] appearanceCodes = MetricsManager.getAppearanceIndexArray();
        for (int eachCode: appearanceCodes) {
            container.addView(createNameTextView(eachCode,context));
            container.addView(createScoreTextView(eachCode,scores,context));
        }

        //Populate Expressions
        container.addView(createHeaderTextView("EXPRESSIONS",context));
        int[] expressionCodes = MetricsManager.getExpressionsIndexArray();
        for (int eachCode: expressionCodes) {
            container.addView(createNameTextView(eachCode,context));
            container.addView(createScoreTextView(eachCode,scores,context));
        }

        //Populate Qualities
        container.addView(createHeaderTextView("QUALITIES",context));
        int[] qualityCodes = MetricsManager.getQualitiesIndexArray();
        for (int eachCode: qualityCodes) {
            container.addView(createNameTextView(eachCode,context));
            container.addView(createScoreTextView(eachCode,scores,context));
        }

        container.addView(createHeaderTextView("TRUTHFULLNESS",context));
        int[] Truthcodes = MetricsManager.getTruthIndexArray();
        for (int eachCode: Truthcodes) {
            container.addView(createNameTextView(eachCode,context));
            container.addView(createScoreTextView(eachCode,scores,context));
        }

        return container;

    }

    private static TextView createHeaderTextView(String headerName, Context context) {
        TextView header = new TextView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        header.setLayoutParams(params);
        header.setTextColor(Color.parseColor("#FFFFFF"));
        header.setGravity(Gravity.CENTER);
        header.setText(headerName);
        header.setTextAppearance(context, R.style.metricCategory);
        return header;
    }

    private static TextView createNameTextView(int metricCode, Context context) {
        TextView name = new TextView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        name.setLayoutParams(params);
        name.setTextColor(Color.parseColor("#FFFFFF"));
        name.setGravity(Gravity.CENTER);
        name.setText(MetricsManager.getMetricUpperCaseName(metricCode));
        name.setTextAppearance(context, R.style.metricName);
        return name;
    }

    private static TextView createScoreTextView(int metricCode, TextView[] scores, Context context) {
        TextView score = new TextView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        score.setLayoutParams(params);
        score.setTextColor(Color.parseColor("#FFFFFF"));
        score.setGravity(Gravity.CENTER);
        score.setPadding(0, 0, 0, 10);
        score.setTextAppearance(context,R.style.metricScore);
        scores[metricCode] = score;
        return score;
    }


}
