sub qsort {
    @_ < 2 ? return @_ : qsort(grep $_ < $_[0], @_[1..$#_]), $_[0], qsort(grep $_ >= $_[0], @_[1..$#_]);
}

print &qsort(qw/7 8 5 4 3 2 1 6/);

# Dirty & Ugly