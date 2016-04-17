package co.theasi.plotly

/** Options controlling how scatter and line plots are plotted. */
case class ScatterOptions(
  name: Option[String],
  xAxis: Option[Int],
  yAxis: Option[Int],
  mode: Seq[ScatterMode.Value],
  text: Option[TextValue],
  marker: MarkerOptions
) extends SeriesOptions[ScatterOptions] {

  /** Set the name of the series */
  def name(newName: String) = copy(name = Some(newName))
  def xAxis(newXAxis: Int) = copy(xAxis = Some(newXAxis))
  def yAxis(newYAxis: Int): ScatterOptions = copy(yAxis = Some(newYAxis))

  /** Set the axis pair on which this scatter is plotted.
    *
    * This should be used in conjunction with the
    * `.ref` method of the plot's layout.
    *
    * @example {{{
    * // layout with three subplots
    * val layout = RowLayout(3)
    *
    * // plot on middle subplot
    * val options = ScatterOptions().onAxes(layout.ref(1))
    * }}}
    */
  def onAxes(axesRef: (Int, Int)): ScatterOptions = {
    val (xAxisRef, yAxisRef) = axesRef
    copy(xAxis = Some(xAxisRef), yAxis = Some(yAxisRef))
  }

  /** Set the line mode.
    *
    * Possible arguments are `ScatterMode.Marker`, `ScatterMode.Line`
    * and `ScatterMode.Text`. These are additive:
    * `.mode(ScatterMode.Marker, ScatterMode.Line)` will show both
    * markers and a line between them.
    *
    * `ScatterMode.Text` indicates that the text corresponding to
    * a particular point should always be shown (by default, it is
    * shown when the user hovers over the point).
    */
  def mode(newMode: ScatterMode.Value, rest: ScatterMode.Value*)
  : ScatterOptions = mode(newMode +: rest)

  /** Set the line mode.
    *
    * Possible arguments are `ScatterMode.Marker`, `ScatterMode.Line`
    * and `ScatterMode.Text`. These are additive:
    * `.mode(List(ScatterMode.Marker, ScatterMode.Line))`
    * will show both markers and a line between them.
    *
    * `ScatterMode.Text` indicates that the text corresponding to
    * a particular point should always be shown (by default, it is
    * shown when the user hovers over the point).
    */
  def mode(newModes: Iterable[ScatterMode.Value]): ScatterOptions =
    copy(mode = newModes.toSeq)

  /** Set the text labels for points in this series.
    *
    * This sets the same label for every point.
    */
  def text(newText: String): ScatterOptions =
    copy(text = Some(StringText(newText)))

  /** Set the text labels for points in this series.
    *
    * This sets a different label for each point. The iterator
    * `newText` must be the same length as the data series.
    */
  def text[T: Writable](newText: Iterable[T]): ScatterOptions = {
    val textAsPType = newText.map { implicitly[Writable[T]].toPType }
    copy(text = Some(IterableText(textAsPType)))
  }

  /** Set the text labels for points in this series.
    *
    * This sets the labels from data that is already in Plotly.
    *
    * @param src String of format `$fileId:$columnUid`, where
    * `$fileId` is the id of a grid in Plotly and column uid is
    * the id of a column in that grid.
    *
    * @example {{{
    * import co.theasi.plotly._
    * val gridFile = writer.GridFile.fromFileName("lowest-oecd-votes-cast-grid")
    *
    * val fileId = gridFile.fileId
    * val columnUid = gridFile.columnUids("y-0")
    *
    * val textSrc = s"$fileId:$columnUid"
    *
    * val xs = (1 to 10)
    * val ys = (1 to 10)
    * val p = Plot().withScatter(xs, ys, ScatterOptions()
    *   .textSrc(textSrc))
    *
    * draw(p, "text-src-example")
    * }}}
    */
  def textSrc(src: String): ScatterOptions =
    copy(text = Some(SrcText(src)))

  def marker(newMarker: MarkerOptions): ScatterOptions =
    copy(marker = newMarker)

  def updatedMarker(updater: MarkerOptions => MarkerOptions)
  : ScatterOptions = {
    val newMarker = updater(marker)
    marker(newMarker)
  }

}

object ScatterOptions {
  def apply(): ScatterOptions = ScatterOptions(
    name = None,
    xAxis = None,
    yAxis = None,
    mode = Seq.empty,
    text = None,
    marker = MarkerOptions()
  )
}

object ScatterMode extends Enumeration {
  val Marker = Value("markers")
  val Line = Value("lines")
  val Text = Value("text")
}

case class MarkerOptions(
  size: Option[Int],
  color: Option[Color],
  symbol: Option[String],
  lineWidth: Option[Int],
  lineColor: Option[Color]
) {

  def size(newSize: Int): MarkerOptions = copy(size = Some(newSize))
  def color(newColor: Color): MarkerOptions =
    copy(color = Some(newColor))
  def color(r: Int, g: Int, b: Int, a: Double): MarkerOptions =
    color(Color.rgba(r, g, b, a))
  def color(r: Int, g: Int, b: Int): MarkerOptions =
    color(r, g, b, 1.0)

  def symbol(newSymbol: String): MarkerOptions =
    copy(symbol = Some(newSymbol))

  def lineWidth(newLineWidth: Int): MarkerOptions =
    copy(lineWidth = Some(newLineWidth))
  def lineColor(newLineColor: Color): MarkerOptions =
    copy(lineColor = Some(newLineColor))
  def lineColor(r: Int, g: Int, b: Int, a: Double): MarkerOptions =
    lineColor(Color.rgba(r, g, b, a))
  def lineColor(r: Int, g: Int, b: Int): MarkerOptions =
    lineColor(r, g, b, 1.0)
}

object MarkerOptions {
  def apply(): MarkerOptions = MarkerOptions(
    size = None,
    color = None,
    symbol = None,
    lineWidth = None,
    lineColor = None
  )
}
