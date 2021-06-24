<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/2000/svg"
	xmlns:xlink="http://www.w3.org/1999/xlink">

	<xsl:output method="xml" indent="yes" encoding="UTF-8"
		doctype-public="-//W3C//DTD SVG 1.1//EN" doctype-system="http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd" />

	<!-- Déclaration de quelques variables utiles (largeur des barres, espacement 
		etc.) -->
	<xsl:param name="Ox" select="0" />
	<xsl:param name="Oy" select="-350" />
	<xsl:param name="largeur_barre" select="200" />
	<xsl:param name="pas_y" select="0.3" />
	<xsl:param name="intervalle" select="15" />

	<!-- Variables pour positionner le texte -->
	<xsl:param name="positionTitreX" select="0" />
	<xsl:param name="positionTitreY" select="50" />

	<!-- règles par défaut -->
	<xsl:template match="*|text()">
	</xsl:template>

	<xsl:template match="/">

		<!-- On met la balise svg et on indique que l'image va prendre tout l'écran 
			et on indique le pas de notre abscisse / ordonnée -->
		<svg width="100%" height="100%" viewBox="0 0 1500 1400">
			<title>Montant des offres faites par chaque utilisateur</title>
			<xsl:call-template name="titre_graphique" />

			<!-- On met notre graphique dans un groupe pour pouvoir le traiter par 
				la suite -->
			<g id="graphique_encheres">
				<g transform="rotate(180, {600}, {500})">
					<!-- Etant donné que le graphique est à l'envers, lorsqu'on le remettra 
						à l'endroit, on aura les dernièeres enchères en premiers. -->
					<!-- Il faut donc les trier par ordre décroissant -->
					<xsl:apply-templates select="//items/item_tuple" >
					<xsl:sort order="descending" select="./itemno"/>
					</xsl:apply-templates>
				</g>
			</g>
		</svg>
	</xsl:template>



	<!-- On va matcher tous les tuples d'item -->
	<xsl:template match="item_tuple">
		<!-- Ici il s'agit de matcher toutes les enchères sur cet item -->
		<xsl:variable name="itemno" select="./itemno" />
		<xsl:variable name="item_position" select="position()" />

		<!-- On place le texte décrivant l'objet -->
		<g
			transform="rotate(180, {$Ox + ($item_position - 1)*($largeur_barre + $intervalle)+$largeur_barre div 2}, {$Oy - 30})">

			<text x="{$Ox + ($item_position - 1)*($largeur_barre + $intervalle)}"
				y="{$Oy - 20}">
				<xsl:value-of select="$itemno" />
			</text>
		</g>

		<!-- Sélection de toutes les enchères pour l'item courant en passant en 
			paramètre la position de l'item -->
		<xsl:apply-templates select="//bids/bid_tuple[./itemno=$itemno]">
			<xsl:with-param name="item_position" select="$item_position" />
		</xsl:apply-templates>

	</xsl:template>

	<!-- Ici on va gérer la partie du graphique des enchères -->
	<xsl:template match="bid_tuple">

		<xsl:param name="item_position" />

		<xsl:variable name="bid_position" select="position()" />

		<xsl:variable name="itemno" select="./itemno" />
		<xsl:variable name="userid" select="./userid/text()" />

		<!-- On calcule la somme des enchères précedentes. On prend soin de la 
			mettre à 0 pour traiter le cas où la positon est de 1 :) -->
		<xsl:variable name="somme_encheres_precedentes">
			<xsl:choose>
				<xsl:when test="$bid_position > 1">
					<xsl:value-of
						select="sum(//bid_tuple[itemno=$itemno][$bid_position]/bid/preceding::bid_tuple[itemno=$itemno]/bid)" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="0" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>



		<xsl:variable name="couleur">
			<xsl:choose>
				<xsl:when test="position() mod 5 = 1">
					red
				</xsl:when>
				<xsl:when test="position() mod 5 = 2">
					blue
				</xsl:when>
				<xsl:when test="position() mod 5 = 3">
					green
				</xsl:when>
				<xsl:when test="position() mod 5 = 4">
					purple
				</xsl:when>
				<xsl:otherwise>
					orange
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<!-- On dessine le rectangle en passant au template les paramètres necessaires -->
		<xsl:call-template name="dessinner_rectangle">
			<xsl:with-param name="itemno" select="./itemno/text()" />
			<xsl:with-param name="bid_value" select="bid" />
			<xsl:with-param name="bider"
				select="//user_tuple[./userid=$userid]/name" />
			<xsl:with-param name="item_position" select="$item_position" />
			<xsl:with-param name="couleur" select="$couleur" />
			<xsl:with-param name="userid" select="userid" />
			<!-- On va calculer la sommes des précédentes enchères pour placer la 
				nouvelle barre en ordonnées -->
			<xsl:with-param name="somme_encheres_precedentes"
				select="$somme_encheres_precedentes" />
		</xsl:call-template>
	</xsl:template>

	<!-- Cette règle va dessiner le rectangle avec les bons paramètres -->
	<xsl:template name="dessinner_rectangle">

		<!-- Récupération des paramètres, on a besoin de -->
		<!-- Le numéro de l'item (itemno) -->
		<!-- la valeur de l'enchere (value) -->
		<!-- l'ID de l'encherisseur (bider) -->
		<!-- la position de l'item courant(item_position) -->
		<!-- la position de l'enchère (bid_position) -->
		<xsl:param name="itemno" />
		<xsl:param name="bid_value" />
		<xsl:param name="bider" />
		<xsl:param name="item_position" />
		<xsl:param name="couleur" />
		<xsl:param name="somme_encheres_precedentes" />
		<xsl:param name="userid" />

		<xsl:variable name="text_barre">
			<xsl:value-of select="$bider" />
			_
			<xsl:value-of select="$itemno" />
			_
			<xsl:value-of select="$bid_value" />
		</xsl:variable>

		<!-- Ok c'est partie :) -->

		<!-- On défini une description qui sera affichée lorsque la souris survollera 
			la barre -->
		<g style="display:none;font-size:50px;" transform="rotate(180, 600,100)"
			id="{$text_barre}">
			<text x="1500" y="-400">
				Description de l'objet :
				<xsl:value-of select="//item_tuple[itemno=$itemno]/description" />
			</text>
			<text x="1500" y="-300">
				Encherisseur :
				<xsl:value-of select="$bider" />
			</text>

			<text x="1500" y="-200">
				Montant de l'enchere :
				<xsl:value-of select="$bid_value" />
			</text>
			<text x="1500" y="-100">
				Date :
				<xsl:value-of
					select="//bid_tuple[userid=$userid and itemno=$itemno]/bid_date" />
			</text>

		</g>

		<!-- On place le rectangle -->
		<g>
			<rect x="{$Ox + ($item_position - 1)*($largeur_barre + $intervalle)}"
				y="{$Oy + $somme_encheres_precedentes * $pas_y}" width="{$largeur_barre}"
				height="{$bid_value * $pas_y}" style="fill:{$couleur}">

				<!-- petite animation -->
				<animate fill="freeze" dur="0.1s" to="yellow" from="{$couleur}"
					attributeName="fill" begin="mouseover" />
				<animate fill="freeze" dur="0.1s" to="{$couleur}" from="blue"
					attributeName="fill" begin="mouseout" />

				<animate xlink:href="#{$text_barre}" fill="freeze" dur="0.1s"
					to="block" from="none" attributeName="display" begin="mouseover" />
				<animate xlink:href="#{$text_barre}" fill="freeze" dur="0.1s"
					to="none" from="block" attributeName="display" begin="mouseout" />
			</rect>
		</g>

		<g
			transform="rotate(180, {$Ox + ($item_position - 1)*($largeur_barre + $intervalle) + $largeur_barre div 2}, {$Oy + $somme_encheres_precedentes * $pas_y + ($bid_value * $pas_y) - $intervalle})">
			<text x="{$Ox + ($item_position - 1)*($largeur_barre + $intervalle)}"
				y="{$Oy + $somme_encheres_precedentes * $pas_y + ($bid_value * $pas_y)-20}">
				<xsl:value-of select="$bider" />
				:
				<xsl:value-of select="$bid_value" />
			</text>
		</g>

	</xsl:template>

	<!-- Création d'un template pour écrire le titre du graphique -->
	<xsl:template name="titre_graphique">
		<text x="{$positionTitreX}" y="{$positionTitreY}"
			style="fill:blue;stroke:#abcdef;font-size:50px;">
			Montant des offres et des
			encherisseurs pour chaque objet
		</text>
	</xsl:template>

</xsl:stylesheet>