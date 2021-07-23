package me.rerere.zhiwang.ui.screen.index.page

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import me.rerere.zhiwang.ui.public.XiaoZuoWen
import me.rerere.zhiwang.ui.screen.index.IndexScreenVideoModel
import me.rerere.zhiwang.util.android.getClipboardContent
import me.rerere.zhiwang.util.format.formatToString
import me.rerere.zhiwang.util.noRippleClickable
import java.text.DateFormat
import java.util.*

@ExperimentalAnimationApi
@Composable
fun Content(indexScreenVideoModel: IndexScreenVideoModel, scaffoldState: ScaffoldState) {
    // val coroutineScope = rememberCoroutineScope()
    val response by indexScreenVideoModel.queryResult.observeAsState()
    var error by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    // 返回处理
    // 显示了查重结果的时候点击返回键会清空查重结果，方便重新查重
    BackHandler(response != null) {
        indexScreenVideoModel.resetResult()
    }

    // 查重页面
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // 输入框
        Box(contentAlignment = Alignment.BottomEnd) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .let {
                        if (response == null) {
                            it.height(200.dp)
                        } else {
                            it.wrapContentHeight()
                        }
                    }
                    .padding(16.dp),
                value = indexScreenVideoModel.content,
                onValueChange = {
                    if (it.length >= 10) {
                        error = false
                    }
                    indexScreenVideoModel.content = it
                },
                label = {
                    Text(text = "输入要查重的小作文, 至少10个字哦")
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(5.dp),
                isError = error,
                maxLines = if (response == null) 8 else 1
            )

            // 输入框上的按钮
            Row(
                modifier = Modifier
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 从剪贴板粘贴
                androidx.compose.animation.AnimatedVisibility(visible = indexScreenVideoModel.queryResult.value == null) {
                    Icon(modifier = Modifier.noRippleClickable {
                        val text = context.getClipboardContent()
                        text?.let {
                            indexScreenVideoModel.content = it
                        } ?: kotlin.run {
                            Toast.makeText(context, "剪贴板没有内容", Toast.LENGTH_SHORT).show()
                        }
                    }, imageVector = Icons.Default.ContentPaste, contentDescription = null)
                }

                Spacer(modifier = Modifier.width(4.dp))

                // 清空
                androidx.compose.animation.AnimatedVisibility(visible = indexScreenVideoModel.content.isNotEmpty()) {
                    Icon(modifier = Modifier.noRippleClickable {
                        indexScreenVideoModel.content = ""
                        indexScreenVideoModel.queryResult.value = null
                    }, imageVector = Icons.Default.Clear, contentDescription = null)
                }
            }
        }
        val focusManager = LocalFocusManager.current

        // 查重按钮
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            onClick = {
                focusManager.clearFocus()

                if (indexScreenVideoModel.content.length < 10) {
                    // 小作文长度不够
                    error = true
                    Toast.makeText(context, "小作文至少需要10个字哦", Toast.LENGTH_SHORT).show()
                } else if (System.currentTimeMillis() - indexScreenVideoModel.lastQuery <= 5000L) {
                    Toast.makeText(context, "请等待 5 秒再查重哦！", Toast.LENGTH_SHORT).show()
                } else {
                    // 开始查询
                    indexScreenVideoModel.resetResult()
                    indexScreenVideoModel.query()
                }
            }) {
            Text(text = "立即查重捏 🤤")
        }

        // 加载动画
        if (indexScreenVideoModel.loading) {
            val width = listOf(0.9f, 1f, 0.87f, 0.83f, 0.89f, 0.86f)
            repeat(6) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(width[it])
                        .height(90.dp)
                        .padding(16.dp)
                        .placeholder(visible = true, highlight = PlaceholderHighlight.shimmer())
                )
            }
        }

        // 加载错误
        if (indexScreenVideoModel.error) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp), contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(text = "加载错误！😨", fontWeight = FontWeight.Bold)
                    Text(text = "请检查你的网络连接，或者可能是查重服务器维护中")
                }
            }
        }

        // 结果
        response?.let {
            when (it.code) {
                0 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        //elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "总文字复制比: ${(it.data.rate * 100).formatToString()}%",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 23.sp,
                                    modifier = Modifier.padding(4.dp),
                                    color = MaterialTheme.colors.secondary
                                )
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .size(25.dp),
                                    progress = it.data.rate.toFloat()
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            // "复制查重结果"按钮
                            OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = {
                                val clipboardManager =
                                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboardManager.setPrimaryClip(
                                    ClipData.newPlainText(
                                        null, """
                                    查重结果:
                                    * 查重时间: ${
                                            DateFormat.getDateInstance(0, Locale.CHINA).format(
                                                Date()
                                            )
                                        }
                                    * 文字复制率: ${(it.data.rate * 100).formatToString()}%
                                    * 首次出现于: ${if (it.data.related.isNotEmpty()) it.data.related[0][2] else "无"}
                                    数据来源于枝网，仅供参考
                                """.trimIndent()
                                    )
                                )
                                Toast.makeText(context, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
                            }) {
                                Text(text = "点击复制查重结果")
                            }
                            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                                // 防止某些乱复制的
                                Text(text = "请自行判别是否原创，请勿到处刷查重报告")
                                Text(text = "A友们都是有素质的人捏")
                            }
                        }
                    }
                    // 查重结果概述
                    Text(
                        text = "相似小作文: (${it.data.related.size}篇)",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                    )
                    // 相似小作文列表

                    it.data.related.forEach { zuowen ->
                        XiaoZuoWen(zuowen)
                    }
                }
                4003 -> {
                    Text(text = "服务器内部错误")
                }
            }
        }

        Spacer(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .height(0.5.dp)
                .background(Color.Gray)
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "数据来源于: https://asoulcnki.asia/",
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "目前仅收录了官方账号下面的小作文，二创下的小作文并未收录，所以查重结果仅供参考哦",
            textAlign = TextAlign.Center
        )
    }
}