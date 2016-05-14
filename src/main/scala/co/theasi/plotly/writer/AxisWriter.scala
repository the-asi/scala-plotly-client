package co.theasi.plotly.writer

import org.json4s._
import org.json4s.JsonDSL._

import co.theasi.plotly.{ViewPort, AxisOptions, CartesianPlot}

object CartesianPlotLayoutWriter {

  def toJson(
    axisIndex: Int,
    viewPort: ViewPort,
    plot: CartesianPlot)
  : JObject = {
    val xAxis = axisToJson(
      axisIndex, viewPort.xDomain, "xaxis", "y", plot.options.xAxis.options)
    val yAxis = axisToJson(
      axisIndex, viewPort.yDomain, "yaxis", "x", plot.options.yAxis.options)

    xAxis ~ yAxis
  }
  

  private def axisToJson(
    axisIndex: Int,
    domain: (Double, Double),
    radix: String,
    anchorRadix: String,
    options: AxisOptions)
  : JObject = {
    val indexString = (if(axisIndex == 1) "" else axisIndex.toString)
    val label = radix + indexString
    val (start, end) = domain
    val body = (
      ("domain" -> List(start, end)) ~
      ("anchor" -> (anchorRadix + indexString)) ~
      axisOptionsToJson(options)
    )
    JObject(JField(label, body))
  }


  private def axisOptionsToJson(options: AxisOptions): JObject = (
    ("title" -> options.title) ~
    ("ticklen" -> options.tickLength) ~
    ("zeroline" -> options.zeroLine) ~
    ("gridwidth" -> options.gridWidth) ~
    ("showgrid" -> options.grid) ~
    ("showline" -> options.line) ~
    ("linecolor" -> options.lineColor.map(ColorWriter.toJson _)) ~
    ("titlefont" -> FontWriter.toJson(options.titleFont)) ~
    ("tickfont" -> FontWriter.toJson(options.tickFont)) ~
    ("autotick" -> options.autoTick) ~
    ("dtick" -> options.tickSpacing) ~
    ("tickcolor" -> options.tickColor.map(ColorWriter.toJson _))
  )

}
