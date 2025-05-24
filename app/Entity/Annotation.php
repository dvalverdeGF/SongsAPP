<?php

namespace App\Entity;

use App\Repository\AnnotationRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: AnnotationRepository::class)]
class Annotation
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    // Posición relativa (por ejemplo, carácter dentro del verso)
    #[ORM\Column(type: 'float')]
    private float $relativeCharPosition;

    // Desplazamiento fino en píxeles para ajuste horizontal
    #[ORM\Column(type: 'float', nullable: true)]
    private ?float $pixelOffset = null;

    // El símbolo o tipo de anotación, podría ser una entidad propia o un string simple
    #[ORM\ManyToOne(inversedBy: 'annotations')]
    #[ORM\JoinColumn(nullable: false)]
    private ?AnnotationSymbol $symbol = null;

    #[ORM\ManyToOne(inversedBy: 'annotations')]
    #[ORM\JoinColumn(nullable: false)]
    private ?Verse $verse = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getVerse(): ?Verse
    {
        return $this->verse;
    }

    public function setVerse(?Verse $verse): static
    {
        $this->verse = $verse;

        return $this;
    }

    public function getRelativeCharPosition(): ?float
    {
        return $this->relativeCharPosition;
    }

    public function setRelativeCharPosition(float $relativeCharPosition): static
    {
        $this->relativeCharPosition = $relativeCharPosition;

        return $this;
    }

    public function getPixelOffset(): ?float
    {
        return $this->pixelOffset;
    }

    public function setPixelOffset(?float $pixelOffset): static
    {
        $this->pixelOffset = $pixelOffset;

        return $this;
    }

    public function getSymbol(): ?AnnotationSymbol
    {
        return $this->symbol;
    }

    public function setSymbol(?AnnotationSymbol $symbol): static
    {
        $this->symbol = $symbol;

        return $this;
    }
}
