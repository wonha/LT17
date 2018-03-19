package main

import "fmt"

func main() {
	c := make(chan int)
	c <- 1 // deadlock
	fmt.Println(<-c)
}
