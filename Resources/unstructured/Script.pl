	#!/usr/bin/perl

	open(ARTICLE,"<articles_grand_100_pourcent.txt") || die ("Erreur d'ouverture de ARTICLE");
	open(SORTIE_APRIORI,">transa.txt") || die ("Erreur d'ouverture de SORTIE_APRIORI");
	open(MOTS,"<mots.lst") || die ("Erreur d'ouverture de MOTS");
	
	while($mot = <MOTS>) {
		chomp($mot);
		$mot =~ tr/ÀÁÂÃÄÅÇÈÉÊËÌÍÎÏÑÒÓÔÕÖÙÚÛÜİàáâãäåçèéêëìíîïñòóôõöùúûüıÿ/AAAAAACEEEEIIIINOOOOOUUUUYaaaaaaceeeeiiiinooooouuuuyy/;
	    push(@mots,$mot);
	    }
	close(MOTS);
	
	my (%saw,@out)=();
	undef %saw;
	@out = sort(grep(!$saw{$_}++, @mots));
		
	$premier_mot = 1;
	foreach (@out) {
		if(not $premier_mot) {        # gestion de la virgule
	    	print SORTIE_APRIORI "\t";
	    }
	    $premier_mot = 0;
		print SORTIE_APRIORI $_;	
	}
	print SORTIE_APRIORI "\n";
	
	$nbArticles = 0;
	while($ligne = <ARTICLE>) {
		$ligne =~ tr/ÀÁÂÃÄÅÇÈÉÊËÌÍÎÏÑÒÓÔÕÖÙÚÛÜİàáâãäåçèéêëìíîïñòóôõöùúûüıÿ/AAAAAACEEEEIIIINOOOOOUUUUYaaaaaaceeeeiiiinooooouuuuyy/;
		$nbArticles++;
		$premier_mot = 1; # obligatoire sinon virgule en debut de ligne
		foreach (@out) {
		    $mot = $_;
		    if(not $premier_mot) {        # gestion de la virgule
		        print SORTIE_APRIORI "\t";
		    }
		    $premier_mot = 0;
		    if($ligne =~ /\b${mot}\b/) {
		    	print SORTIE_APRIORI "1";      # ecriture du mot sans les espaces
			} else { print SORTIE_APRIORI "0";}
						      
		}		 
		print SORTIE_APRIORI "\n"; # saut de lignes pour indiquer un nouvel article

	}

	close(ARTICLE);
	close(SORTIE_APRIORI);

