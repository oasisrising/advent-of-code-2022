#!/usr/bin/perl
use strict;
use warnings;

my $fileToProcess = $ARGV[0];
my $fileHandle = undef;

open($fileHandle,  "< :encoding(UTF-8)", $fileToProcess);

my $total = 0;
my @calories;

while (<$fileHandle>) {

    if ($_ =~ /^\n/) {

        push(@calories, $total);
        $total = 0;
    }
    else {
        chomp($_);
        my $number = int($_);
        $total += $number;
    }
}


my @sortedCalories = sort { $b <=> $a } @calories;
my $maxNumber = 0;
for(my $index = 0; $index < 3; $index++) {
 $maxNumber += $sortedCalories[$index];
}
print "\nThe answer for part 1 is $sortedCalories[0]\n";

print "\nThe answer for part 2 is $maxNumber\n";