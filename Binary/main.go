package main

import (
	"os"
	"os/exec"
	"strings"
)

//go:generate go-winres make
func main() {
	dirs, err := os.ReadDir("./")
	if err != nil {
		panic(err)
	}

	var jarFile string

	for _, entry := range dirs {
		if entry.Type().IsDir() {
			continue
		}

		if strings.HasSuffix(entry.Name(), ".jar") && strings.HasPrefix(entry.Name(), "editor") {
			jarFile = entry.Name()
			break
		}
	}

	cmd := exec.Command("javaw", "--module-path", "lib", "--add-modules", "javafx.controls", "-jar", jarFile)

	err = cmd.Run()
	if err != nil {
		panic(err)
	}
}
