package com.arer.app.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.arer.app.data.local.entity.HMProfileEntity
import com.arer.app.data.local.entity.SchoolProfileEntity
import com.arer.app.domain.model.Money
import com.arer.app.domain.model.MonthlyMdmCalculationResult
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfReportGenerator {

    fun generatePdf(
        context: Context,
        result: MonthlyMdmCalculationResult,
        school: SchoolProfileEntity?,
        hm: HMProfileEntity?
    ): File {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size at 72 DPI
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 12f
        }
        val titlePaint = Paint().apply {
            color = Color.rgb(249, 115, 22) // ARER Orange #F97316
            textSize = 18f
            isFakeBoldText = true
        }
        val boldPaint = Paint().apply {
            color = Color.BLACK
            textSize = 12f
            isFakeBoldText = true
        }
        val linePaint = Paint().apply {
            color = Color.GRAY
            strokeWidth = 1f
        }

        val startX = 40f
        var startY = 50f

        // Title
        canvas.drawText("ARER APP — MONTHLY MDM REPORT", startX, startY, titlePaint)
        startY += 25f

        // School Information
        canvas.drawText("School Name: ${school?.schoolName ?: "N/A"}", startX, startY, boldPaint)
        startY += 18f
        canvas.drawText("UDISE Code: ${school?.udiseCode ?: "N/A"} | School Code: ${school?.schoolCode ?: "N/A"}", startX, startY, paint)
        startY += 18f
        canvas.drawText("District: ${school?.district ?: "N/A"} | Taluk: ${school?.taluk ?: "N/A"} | Cluster: ${school?.cluster ?: "N/A"}", startX, startY, paint)
        startY += 18f
        canvas.drawText("Report Period (Month): ${result.yearMonth}", startX, startY, boldPaint)
        startY += 18f
        val genDateStr = SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.US).format(Date())
        canvas.drawText("Generated On: $genDateStr", startX, startY, paint)
        startY += 25f

        // Summary Line
        canvas.drawLine(startX, startY, 555f, startY, linePaint)
        startY += 20f
        canvas.drawText("Working Days: ${result.totalWorkingDays}   |   MDM Days: ${result.totalMdmDays}   |   Students Served: ${result.totalStudentsServed}", startX, startY, boldPaint)
        startY += 25f
        canvas.drawLine(startX, startY, 555f, startY, linePaint)
        startY += 25f

        // Table Header
        canvas.drawText("Sl.", startX, startY, boldPaint)
        canvas.drawText("Item Name (English / ಕನ್ನಡ)", startX + 40f, startY, boldPaint)
        canvas.drawText("Rate", startX + 300f, startY, boldPaint)
        canvas.drawText("Qty", startX + 370f, startY, boldPaint)
        canvas.drawText("Total Amount", startX + 430f, startY, boldPaint)
        startY += 8f
        canvas.drawLine(startX, startY, 555f, startY, linePaint)
        startY += 20f

        // Item Rows
        var slNo = 1
        for (item in result.itemResults) {
            canvas.drawText("$slNo", startX, startY, paint)
            canvas.drawText("${item.englishName} / ${item.kannadaName}", startX + 40f, startY, paint)
            canvas.drawText(Money(item.ratePaise).formatInRupees(), startX + 300f, startY, paint)
            canvas.drawText("${item.applicableQuantity}", startX + 370f, startY, paint)
            canvas.drawText(Money(item.amountPaise).formatInRupees(), startX + 430f, startY, paint)
            startY += 22f
            slNo++
        }

        canvas.drawLine(startX, startY, 555f, startY, linePaint)
        startY += 25f

        // Grand Total
        canvas.drawText("GRAND TOTAL:", startX + 300f, startY, boldPaint)
        canvas.drawText(Money(result.grandTotalPaise).formatInRupees(), startX + 430f, startY, boldPaint)
        startY += 60f

        // Signature Area
        val sigX = 380f
        canvas.drawLine(sigX, startY, 555f, startY, linePaint)
        startY += 18f
        canvas.drawText("Head Master / ಮುಖ್ಯೋಪಾಧ್ಯಾಯರು", sigX, startY, boldPaint)
        startY += 15f
        canvas.drawText(hm?.hmName ?: "HM Name", sigX, startY, paint)
        startY += 15f
        canvas.drawText(school?.schoolName ?: "School Name", sigX, startY, paint)

        pdfDocument.finishPage(page)

        val file = File(context.cacheDir, "MDM_Report_${result.yearMonth}.pdf")
        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()

        return file
    }
}
