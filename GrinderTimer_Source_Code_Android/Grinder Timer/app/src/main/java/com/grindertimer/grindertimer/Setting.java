package com.grindertimer.grindertimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class Setting extends AppCompatActivity {

    LinearLayout firmwareUpdateLayout, pauseLayout, powerThresholdLayout, waitLayout,
        advancedSettingsLayout, tutorialLayout, contactLayout, showPowerLayout, showAdvancedLayout;
    Switch showPowerSwitch, showAdvancedSwitch;
    TextView pauseDurationValue, powerThresholdValue, waitingPeriodValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar        = findViewById(R.id.toolbar);
        tutorialLayout         = findViewById(R.id.tutorialLayout);
        showPowerSwitch        = findViewById(R.id.showPowerSwitch);
        showPowerLayout        = findViewById(R.id.showPowerLayout);
        showAdvancedLayout     = findViewById(R.id.showAdvancedLayout);
        showAdvancedSwitch     = findViewById(R.id.showAdvancedSwitch);
        waitLayout             = findViewById(R.id.waitLayout);
        pauseLayout            = findViewById(R.id.pauseLayout);
        contactLayout          = findViewById(R.id.contactLayout);
        advancedSettingsLayout = findViewById(R.id.advancedSettingsLayout);
        pauseDurationValue     = findViewById(R.id.textPauseDurationValue);
        powerThresholdLayout   = findViewById(R.id.powerThresholdLayout);
        powerThresholdValue    = findViewById(R.id.textPowerThresholdValue);
        waitingPeriodValue     = findViewById(R.id.textWaitingPeriodValue);

        checkAdvancedSettingsLayoutVisibility();
        showPowerSwitch.setChecked(Constant.showPower);
        showAdvancedSwitch.setChecked(Constant.showAdvanced);
        powerThresholdValue.setText(Integer.toString(Constant.power)+" w");
        waitingPeriodValue.setText(Integer.toString(Constant.waiting)+" ms");
        pauseDurationValue.setText(Double.toString(new Double(Constant.pauseDuration)/1000) +" s");

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });

        tutorialLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.grindertimer.com/tutorial"));
                startActivity(browserIntent);
            }
        });

        contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:info@grindertimer.com"));
                startActivity(browserIntent);
            }
        });

        showPowerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Constant.showPower = isChecked;
            }
        });

        showPowerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.showPower = !Constant.showPower;
                showPowerSwitch.setChecked(Constant.showPower);
            }
        });

        showAdvancedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Constant.showAdvanced = isChecked;
                checkAdvancedSettingsLayoutVisibility();
            }
        });

        showAdvancedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.showAdvanced = !Constant.showAdvanced;
                showAdvancedSwitch.setChecked(Constant.showAdvanced);
                checkAdvancedSettingsLayoutVisibility();
            }
        });

        pauseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog      = new Dialog(Setting.this);
                dialog.setContentView(R.layout.puaseduration);
                dialog.setTitle("Set Pause Duration");
                Button btnMinus          = dialog.findViewById(R.id.btnMinus);
                Button btnPlus           = dialog.findViewById(R.id.btnPlus);
                Button btnSet            = dialog.findViewById(R.id.btnSetValue);
                final TextView textValue = dialog.findViewById(R.id.Value);
                textValue.setText(Double.toString(new Double(Constant.pauseDuration)/1000));
                btnMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Constant.pauseDuration > 4000) {
                            Constant.pauseDuration -= 100;
                            textValue.setText(Double.toString(new Double(Constant.pauseDuration)/1000));
                            pauseDurationValue.setText(Double.toString(new Double(Constant.pauseDuration)/1000));
                        }
                    }
                });
                btnPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Constant.pauseDuration +=100;
                        textValue.setText(Double.toString(new Double(Constant.pauseDuration)/1000));
                        pauseDurationValue.setText(Double.toString(new Double(Constant.pauseDuration)/1000));
                    }
                });
                btnSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        powerThresholdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog      = new Dialog(Setting.this);
                dialog.setContentView(R.layout.power);
                dialog.setTitle("Set Power Trash Hold");
                Button btnMinus          = dialog.findViewById(R.id.btnMinus);
                Button btnPlus           = dialog.findViewById(R.id.btnPlus);
                Button btnSet            = dialog.findViewById(R.id.btnSetValue);
                final TextView textValue = dialog.findViewById(R.id.Value);
                textValue.setText(String.valueOf(Constant.power));
                btnMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Constant.power > 50) {
                            Constant.power = Constant.power - 5;
                            textValue.setText(String.valueOf(Constant.power));
                            powerThresholdValue.setText(Integer.toString(Constant.power)+" w");
                        }
                    }
                });
                btnPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Constant.power = Constant.power + 5;
                        textValue.setText(String.valueOf(Constant.power));
                        powerThresholdValue.setText(Integer.toString(Constant.power)+" w");
                    }
                });
                btnSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        waitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog      = new Dialog(Setting.this);
                dialog.setContentView(R.layout.wait);
                dialog.setTitle("Set Waiting Period");
                Button btnMinus          = dialog.findViewById(R.id.btnMinus);
                Button btnPlus           = dialog.findViewById(R.id.btnPlus);
                Button btnSet            = dialog.findViewById(R.id.btnSetValue);
                final TextView textValue = dialog.findViewById(R.id.Value);
                textValue.setText(String.valueOf(Constant.waiting));
                btnMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Constant.waiting > 1000) {
                            Constant.waiting = Constant.waiting - 50;
                            textValue.setText(String.valueOf(Constant.waiting));
                            waitingPeriodValue.setText(Integer.toString(Constant.waiting)+" ms");
                        }
                    }
                });
                btnPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Constant.waiting = Constant.waiting + 50;
                        textValue.setText(String.valueOf(Constant.waiting));
                        waitingPeriodValue.setText(Integer.toString(Constant.waiting) + " ms");
                    }
                });
                btnSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        firmwareUpdateLayout = findViewById(R.id.firmwareUpdationLayout);
        firmwareUpdateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent send = new Intent(getApplicationContext(),Firmware.class);
                startActivity(send);
                overridePendingTransition(R.anim.fragment_enter_pop, R.anim.fragment_exit_pop);
            }
        });
    }
    private void checkAdvancedSettingsLayoutVisibility(){
        if (Constant.showAdvanced)
            advancedSettingsLayout.setVisibility(View.VISIBLE);
        else
            advancedSettingsLayout.setVisibility(View.GONE);
    }
}
