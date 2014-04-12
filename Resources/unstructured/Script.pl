	#!/usr/bin/perl

	open(ARTICLE,"<articles_grand_test.txt") || die ("Erreur d'ouverture de ARTICLE");
	open(SORTIE_APRIORI,">transa.txt") || die ("Erreur d'ouverture de SORTIE_APRIORI");
	open(MOTS,"<mots.lst") || die ("Erreur d'ouverture de MOTS");
	open(CONFIG,">config.txt") || die ("Erreur d'ouverture de CONFIG");
	open(SORTIE_TEMP, ">sortie.txt") || die ("Erreur d'ouverture");
	$support = 10;
	$gne = 5;

	while($mot = <MOTS>) {
		chomp($mot);
	    push(@mots,$mot);
	    }
	my (%saw,@out)=();
	undef %saw;
	@out = sort(grep(!$saw{$_}++, @mots));
	print CONFIG @out.length."\n";

	
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
			 if($mot eq 'france'){print SORTIE_TEMP $ligne;}
		      } else { print SORTIE_APRIORI "0";}
						      
		}		 
		print SORTIE_APRIORI "\n"; # saut de lignes pour indiquer un nouvel article

	}
	print CONFIG $nbArticles."\n".$support."\n".$gne;

	close(ARTICLE);
	close(SORTIE_APRIORI);

