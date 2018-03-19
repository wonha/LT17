package main

import (
	"fmt"
	"time"
)

func main() {
	tasks := make([]string, 3)
	tasks[0] = "cmake .."
	tasks[1] = "cmake, --build Release"
	tasks[2] = "cpack"

	for _, task := range tasks {
		go func() {
			fmt.Println(task)
		}()
	}
    time.Sleep(time.Second)
}