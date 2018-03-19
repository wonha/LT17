package main

import (
	"fmt"
	"sync"
)

func main() {
	var wait sync.WaitGroup
	wait.Add(3)

	// goroutine with anonymous function
	go func() {
		defer wait.Done()
		fmt.Println("Hello")
	}()

	// pass arguments to anonymous function
	go func(msg string) {
		defer wait.Done()
		fmt.Println(msg)
	}("Hi")

	str := "sample string"
	go func(msg string) {
		fmt.Println("shared information: ", str)
	}()

	wait.Wait()
}
