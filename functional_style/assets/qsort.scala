def sort(xs: List[Int]): List[Int] = {
  xs match {
    case Nil => Nil
    case x :: xx => {
      val (lo, hi) = xx.partition(_ < x)
      sort(lo) ++ (x :: sort(hi))
    }
  }
}