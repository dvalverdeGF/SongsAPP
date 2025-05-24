<?php

namespace App\Entity;

use App\Repository\ChordVariantRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: ChordVariantRepository::class)]
class ChordVariant
{

    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\ManyToOne]
    #[ORM\JoinColumn(nullable: false)]
    private ChordRoot $root;

    #[ORM\ManyToOne]
    private ?ChordAccidental $accidental = null;

    #[ORM\ManyToOne]
    private ?ChordQuality $quality = null;

    #[ORM\ManyToOne]
    private ?ChordExtension $extension = null;

    #[ORM\ManyToOne]
    private ?ChordModifier $modifier = null;

    #[ORM\Column(length: 15)]
    private string $name; // Ej: C#m7add9

    #[ORM\Column(length: 20)]
    private string $nameLatin; // Ej: Do menor séptima añadida 9ª

    public function getFullName(): string
    {
        $name = $this->root->getName(); // Ej: "C", "D", etc.

        return $name. ' ' .
            $this->buildModifiers();
    }
    public function getFullNameLatin(): string
    {
        $name = $this->root->getNameLatin(); // Ej: "C", "D", etc.

        return $name. ' ' .
            $this->buildModifiers();
    }
    private function buildModifiers(): string
    {
        $modifiers = [];
        if ($this->accidental) {
            $modifiers[] = $this->accidental->getSymbol();
        }
        if ($this->quality) {
            $modifiers[] = $this->quality->getCode();
        }
        if ($this->extension) {
            $modifiers[] = $this->extension->getSymbol();
        }
        if ($this->modifier) {
            $modifiers[] = $this->modifier->getSymbol();
        }

        return implode('', $modifiers);
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getName(): ?string
    {
        return $this->name;
    }

    public function setName(string $name): static
    {
        $this->name = $name;

        return $this;
    }

    public function getNameLatin(): ?string
    {
        return $this->nameLatin;
    }

    public function setNameLatin(string $nameLatin): static
    {
        $this->nameLatin = $nameLatin;

        return $this;
    }

    public function getRoot(): ?ChordRoot
    {
        return $this->root;
    }

    public function setRoot(?ChordRoot $root): static
    {
        $this->root = $root;

        return $this;
    }

    public function getAccidental(): ?ChordAccidental
    {
        return $this->accidental;
    }

    public function setAccidental(?ChordAccidental $accidental): static
    {
        $this->accidental = $accidental;

        return $this;
    }

    public function getQuality(): ?ChordQuality
    {
        return $this->quality;
    }

    public function setQuality(?ChordQuality $quality): static
    {
        $this->quality = $quality;

        return $this;
    }

    public function getExtension(): ?ChordExtension
    {
        return $this->extension;
    }

    public function setExtension(?ChordExtension $extension): static
    {
        $this->extension = $extension;

        return $this;
    }

    public function getModifier(): ?ChordModifier
    {
        return $this->modifier;
    }

    public function setModifier(?ChordModifier $modifier): static
    {
        $this->modifier = $modifier;

        return $this;
    }

}
