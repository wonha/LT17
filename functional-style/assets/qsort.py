def qsort(array):
    smaller = []
    larger = []
    equal = []

    if len(array) > 1:
        pivot = array[0]
        for x in array:
            if x < pivot:
                smaller.append(x)
            elif x > pivot:
                larger.append(x)
            else:
                equal.append(x)

        return qsort(smaller) + equal + qsort(larger)
    else:
        return array

print (qsort([4, 5, 6, 7, 8, 2, 3, 1]))

# Python has method (function)
# List of Steps, Variable to store current step (state), flow control (for, if), Imperative Language, Side effect (mutable variable, I/O, etc) inside function