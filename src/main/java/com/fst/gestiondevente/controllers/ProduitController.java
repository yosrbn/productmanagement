package com.fst.gestiondevente.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fst.gestiondevente.entities.Category;
import com.fst.gestiondevente.entities.Fournisseur;
import com.fst.gestiondevente.entities.Produit;
import com.fst.gestiondevente.repositories.CategoryRepository;
import com.fst.gestiondevente.repositories.FournisseurRepository;
import com.fst.gestiondevente.repositories.ProduitRepository;

@Controller
@RequestMapping("produit")
public class ProduitController 
{
	@Autowired
    ProduitRepository produitRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	FournisseurRepository fournisseurRepository;
	
	@GetMapping("/list")
	public String afficher(Model model)
	{
		 List<Produit> produits = produitRepository.findAll();
		
		    model.addAttribute("produits", produits);
		    return "product/affichage";
	}
	
    @GetMapping("/add")
    public String showAddForm(Model model) {
      
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("fournisseurs", fournisseurRepository.findAll());
        model.addAttribute("produit", new Produit());
        return "product/ajout";
    }
    
    @PostMapping("/add")
    public String ajout(@RequestParam("nom") String nom, @RequestParam("description") String description,
	@RequestParam("prix") double prix, @RequestParam("image") String image, @RequestParam("categoryId") Long categoryId, @RequestParam("fournisseurId") Long fournisseurId)
    {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        Fournisseur fournisseur = fournisseurRepository.findById(fournisseurId).orElse(null);
        
        if (category != null &&fournisseur != null) {
            Produit produit = new Produit(nom, description, prix, image);
            produit.setCategory(category);
            produit.setFournisseur(fournisseur);
            produitRepository.save(produit);
        }
    	return "redirect:list";
	}
    
    @GetMapping("delete/{id}")
    public String supprimer(@PathVariable("id") long id)
    {
    	produitRepository.deleteById(id);
    	return "redirect:../list";
	}

    @GetMapping("update/{id}")
    public String maj(Model model,@PathVariable("id") long id)
    {
    	Optional<Produit> produit=produitRepository.findById(id);
    	model.addAttribute("p",produit);
    	return "product/modification";
	}

    @PostMapping("update")
    public String modifier(@ModelAttribute Produit produit, @RequestParam("categoryId") Long categoryId, @RequestParam("fournisseurId") Long fournisseurId)
    {
    	Category category = categoryRepository.findById(categoryId).orElse(null);
        Fournisseur fournisseur = fournisseurRepository.findById(fournisseurId).orElse(null);
       
        if ((category != null)&&(fournisseur != null))
        {
            produit.setCategory(category);
            produit.setFournisseur(fournisseur);
        }
    
    	produitRepository.save(produit);
    	return "redirect:list";
    
    }
}

