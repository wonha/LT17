package main

func main() {
	ch := make(chan int)

	go func() {
		ch <- 123
	}()

	var i int
	i = <- ch
	println(i)
}