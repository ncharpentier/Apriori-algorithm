#!/usr/bin/perl

open(TICKETS,"<tickets_de_caisse.txt") || die ("Erreur d'ouverture de TICKETS");
open(SORTIE_APRIORI,">transa.txt") || die ("Erreur d'ouverture de SORTIE_APRIORI");

my $colonneAIgnorer = 0;
my @colonnesADiscretiser = (1,5,6);
my $nbLignes;

while (<TICKETS>) {
    @tmp = split;
    push @TdT, [ @tmp ];
    $nbLignes++;
}

foreach $ignore (@colonnesADiscretiser) {
	my @mat;
	for $i ( 1 .. $#TdT ) {
		$mat[$i] = $TdT[$i][$ignore];
	}
	
	@mat = sort(@mat);
	$min = int($mat[1]);
	$premierTiers = int($nbLignes/3);
	$secondTiers = int(2*$nbLignes/3);
	$valUnTiers = int($mat[$premierTiers]);
	$valDeuxTiers = int($mat[$secondTiers]);
	$max = int($mat[$#TdT]) + 1;

	for $i ( 1 .. $#TdT ) {
		$val = $TdT[$i][$ignore];
		chomp($val);
		print TEST $val." ".$moyenne."\n";
		if(int($val) < $valUnTiers) { $TdT[$i][$ignore] = "[".$min."-".$valUnTiers."]"; }
		elsif (int($val) < $valDeuxTiers) { $TdT[$i][$ignore] = "[".$valUnTiers."-".$valDeuxTiers."]"; }
		else { $TdT[$i][$ignore] = "[".$valDeuxTiers."-".$max."]"; }
	}
}

for $i ( 0 .. $#TdT ) {
    for $j ( 0 .. $#{$TdT[$i]} ) {
    	if(not ($j == $colonneAIgnorer)) { print SORTIE_APRIORI "$TdT[$i][$j]\t"; }
    }
    print SORTIE_APRIORI "\n";
}