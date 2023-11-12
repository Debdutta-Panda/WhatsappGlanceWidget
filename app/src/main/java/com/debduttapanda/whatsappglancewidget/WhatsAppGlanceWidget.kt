package com.debduttapanda.whatsappglancewidget

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.Text

class WhatsAppGlanceWidgetReceiver: GlanceAppWidgetReceiver(){
    override val glanceAppWidget: GlanceAppWidget = WhatsAppGlanceWidget()
}

class WhatsAppGlanceWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            var editing by remember{mutableStateOf(false)}
            var mobileNumber by remember{ mutableStateOf("") }
            if(editing){
                Column(
                    modifier = GlanceModifier
                        .background(Color.Green)
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(mobileNumber)
                    Row{
                        repeat(3){
                            NumberButton(number = "${it + 1}") {
                                mobileNumber += "${it+1}"
                            }
                        }
                    }
                    Row{
                        repeat(3){
                            NumberButton(number = "${it + 4}") {
                                mobileNumber += "${it+4}"
                            }
                        }
                    }
                    Row{
                        repeat(3){
                            NumberButton(number = "${it + 7}") {
                                mobileNumber += "${it+7}"
                            }
                        }
                    }

                    Row{
                        Image(
                            provider = ImageProvider(R.drawable.baseline_cleaning_services_24),
                            contentDescription = "Clear",
                            modifier = GlanceModifier
                                .size(40.dp)
                                .clickable {
                                    mobileNumber = ""
                                }
                                .padding(8.dp)
                        )
                        NumberButton(number = "0") {
                            mobileNumber += "0"
                        }
                        Image(
                            provider = ImageProvider(R.drawable.baseline_arrow_back_24),
                            contentDescription = "Back",
                            modifier = GlanceModifier
                                .size(40.dp)
                                .clickable {
                                    val text = mobileNumber
                                    mobileNumber = text.substring(0,text.length-1)
                                }
                                .padding(8.dp)
                        )
                    }
                    Row{
                        Image(
                            provider = ImageProvider(R.drawable.baseline_send_24),
                            contentDescription = "Send",
                            modifier = GlanceModifier
                                .width(60.dp)
                                .height(40.dp)
                                .clickable {
                                    sendToWhatsapp(context,mobileNumber)
                                    mobileNumber = ""
                                    editing = false
                                }
                                .padding(8.dp)
                        )
                        Image(
                            provider = ImageProvider(R.drawable.baseline_power_settings_new_24),
                            contentDescription = "Close",
                            modifier = GlanceModifier
                                .width(60.dp)
                                .height(40.dp)
                                .clickable {
                                    editing = false
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }
            else{
                Image(
                    provider = ImageProvider(R.drawable.baseline_send_to_mobile_24),
                    contentDescription = "Logo",
                    modifier = GlanceModifier
                        .size(64.dp)
                        .background(Color.Green)
                        .clickable {
                            editing = true
                        }
                )
            }
        }
    }

    private fun sendToWhatsapp(context: Context, mobileNumber: String) {
        val msg = "Hi"
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://api.whatsapp.com/send?phone=$mobileNumber&text=$msg")
            )
                .apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
        )
    }

    @Composable
    fun NumberButton(
        number: String,
        onClick: ()->Unit
    ){
        Box(
            modifier = GlanceModifier
                .size(40.dp)
                .clickable {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ){
            Text(number)
        }
    }
}

