package suzaku.ui.style

trait StylePropOrClass

trait StyleDef extends StylePropOrClass

trait StyleProperty extends StyleDef

trait StyleBaseProperty extends StyleProperty

// meta styles
case class StyleSeq(styles: List[StyleBaseProperty]) extends StyleDef {
  def ++(other: StyleSeq): StyleSeq = StyleSeq(styles ::: other.styles)
}

object StyleSeq {
  def apply(style: StyleBaseProperty*): StyleSeq = StyleSeq(style.toList)
}

// normal style properties
case object EmptyStyle extends StyleBaseProperty

case class Color(color: RGBColor)           extends StyleBaseProperty
case class BackgroundColor(color: RGBColor) extends StyleBaseProperty

case class OutlineWidth(value: WidthDimension) extends StyleBaseProperty
case class OutlineStyle(style: LineStyle)      extends StyleBaseProperty
case class OutlineColor(color: RGBColor)       extends StyleBaseProperty

trait LengthProperty extends StyleBaseProperty {
  def value: LengthDimension
}

trait WidthProperty extends StyleBaseProperty {
  def value: WidthDimension
}

trait LineStyleProperty extends StyleBaseProperty {
  def style: LineStyle
}

trait ColorProperty extends StyleBaseProperty {
  def color: RGBColor
}

// font and text
case class FontFamily(family: List[String])    extends StyleBaseProperty
case class FontSize(size: FontDimension)       extends StyleBaseProperty
case class FontWeight(weight: WeightDimension) extends StyleBaseProperty
case object FontItalics                        extends StyleBaseProperty

object FontFamily {
  def apply(family: String*): FontFamily = FontFamily(family.toList)
}

// properties with directionality
sealed trait Direction
trait DirectionTop    extends Direction
trait DirectionRight  extends Direction
trait DirectionBottom extends Direction
trait DirectionLeft   extends Direction

sealed trait Margin extends Direction with LengthProperty

case class MarginTop(value: LengthDimension)    extends Margin with DirectionTop
case class MarginRight(value: LengthDimension)  extends Margin with DirectionRight
case class MarginBottom(value: LengthDimension) extends Margin with DirectionBottom
case class MarginLeft(value: LengthDimension)   extends Margin with DirectionLeft

sealed trait Padding extends Direction with LengthProperty

case class PaddingTop(value: LengthDimension)    extends Padding with DirectionTop
case class PaddingRight(value: LengthDimension)  extends Padding with DirectionRight
case class PaddingBottom(value: LengthDimension) extends Padding with DirectionBottom
case class PaddingLeft(value: LengthDimension)   extends Padding with DirectionLeft

sealed trait Offset extends Direction with LengthProperty

case class OffsetTop(value: LengthDimension)    extends Offset with DirectionTop
case class OffsetRight(value: LengthDimension)  extends Offset with DirectionRight
case class OffsetBottom(value: LengthDimension) extends Offset with DirectionBottom
case class OffsetLeft(value: LengthDimension)   extends Offset with DirectionLeft

sealed trait BorderWidth extends Direction with WidthProperty

case class BorderWidthTop(value: WidthDimension)    extends BorderWidth with DirectionTop
case class BorderWidthRight(value: WidthDimension)  extends BorderWidth with DirectionRight
case class BorderWidthBottom(value: WidthDimension) extends BorderWidth with DirectionBottom
case class BorderWidthLeft(value: WidthDimension)   extends BorderWidth with DirectionLeft

sealed trait BorderStyle extends Direction with LineStyleProperty

case class BorderStyleTop(style: LineStyle)    extends BorderStyle with DirectionTop
case class BorderStyleRight(style: LineStyle)  extends BorderStyle with DirectionRight
case class BorderStyleBottom(style: LineStyle) extends BorderStyle with DirectionBottom
case class BorderStyleLeft(style: LineStyle)   extends BorderStyle with DirectionLeft

sealed trait BorderColor extends Direction with ColorProperty

case class BorderColorTop(color: RGBColor)    extends BorderColor with DirectionTop
case class BorderColorRight(color: RGBColor)  extends BorderColor with DirectionRight
case class BorderColorBottom(color: RGBColor) extends BorderColor with DirectionBottom
case class BorderColorLeft(color: RGBColor)   extends BorderColor with DirectionLeft

// dimension styles
case class Width(value: LengthUnit)  extends StyleBaseProperty
case class Height(value: LengthUnit) extends StyleBaseProperty

case class MaxWidth(value: LengthUnit)  extends StyleBaseProperty
case class MaxHeight(value: LengthUnit) extends StyleBaseProperty

case class MinWidth(value: LengthUnit)  extends StyleBaseProperty
case class MinHeight(value: LengthUnit) extends StyleBaseProperty

// pseudo classes
trait PseudoClass {
  def props: List[StyleBaseProperty]
}

case class Hover(props: List[StyleBaseProperty]) extends StyleBaseProperty with PseudoClass

case class Active(props: List[StyleBaseProperty]) extends StyleBaseProperty with PseudoClass

// style classes
sealed trait StyleClassProperty extends StyleProperty

case class StyleClasses(styles: List[StyleClass]) extends StyleClassProperty

case class InheritClasses(styles: List[StyleClass]) extends StyleClassProperty

case class ExtendClasses(styles: List[StyleClass]) extends StyleClassProperty

case class RemapClasses(styleMap: Map[StyleClass, List[StyleClass]]) extends StyleBaseProperty

object StyleProperty {
  import boopickle.Default._

  // import specific picklers to prevent huge macro overhead
  import LengthDimension._, WidthDimension._, WeightDimension._, FontDimension._, RGBColor._, LineStyle._

  implicit val styleClassPickler = new StyleClassPickler

  implicit val styleBasePickler = compositePickler[StyleBaseProperty]
  styleBasePickler
    .addConcreteType[EmptyStyle.type]
    .addConcreteType[Color]
    .addConcreteType[BackgroundColor]
    .addConcreteType[FontFamily]
    .addConcreteType[FontSize]
    .addConcreteType[FontWeight]
    .addConcreteType[FontItalics.type]
    .addConcreteType[MarginTop]
    .addConcreteType[MarginRight]
    .addConcreteType[MarginBottom]
    .addConcreteType[MarginLeft]
    .addConcreteType[PaddingTop]
    .addConcreteType[PaddingRight]
    .addConcreteType[PaddingBottom]
    .addConcreteType[PaddingLeft]
    .addConcreteType[OffsetTop]
    .addConcreteType[OffsetRight]
    .addConcreteType[OffsetBottom]
    .addConcreteType[OffsetLeft]
    .addConcreteType[BorderWidthTop]
    .addConcreteType[BorderWidthRight]
    .addConcreteType[BorderWidthBottom]
    .addConcreteType[BorderWidthLeft]
    .addConcreteType[BorderStyleTop]
    .addConcreteType[BorderStyleRight]
    .addConcreteType[BorderStyleBottom]
    .addConcreteType[BorderStyleLeft]
    .addConcreteType[BorderColorTop]
    .addConcreteType[BorderColorRight]
    .addConcreteType[BorderColorBottom]
    .addConcreteType[BorderColorLeft]
    .addConcreteType[OutlineWidth]
    .addConcreteType[OutlineStyle]
    .addConcreteType[OutlineColor]
    .addConcreteType[Width]
    .addConcreteType[Height]
    .addConcreteType[MaxWidth]
    .addConcreteType[MaxHeight]
    .addConcreteType[MinWidth]
    .addConcreteType[MinHeight]
    .addConcreteType[RemapClasses]
    .addConcreteType[Hover]
    .addConcreteType[Active]

  val stylePickler = compositePickler[StyleProperty]
    .join(styleBasePickler)
    .addConcreteType[StyleClasses]
    .addConcreteType[InheritClasses]
    .addConcreteType[ExtendClasses]
}
