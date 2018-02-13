package main

import "fmt"

func main() {
	ch := make(chan int, 1)

	ch <- 101

	fmt.Println(<-ch)
}
