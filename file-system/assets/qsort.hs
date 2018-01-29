
qsort [] = []
qsort (x:xs) = qsort smaller ++ [x] ++ qsort larger
    where
        smaller = [a | a <- xs, a <= x]
        larger = [b | b <- xs, b > x]

-- { Stateless, Program composed with only input/output of function (no assignment to variable, it is declaration), No flow control keyword (using recursion), NO side effect -> FRP, no race condition }