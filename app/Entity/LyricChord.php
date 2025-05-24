<?php

namespace App\Entity;

use App\Repository\LyricChordRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: LyricChordRepository::class)]
class LyricChord
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(type: 'float')]
    private float $relativeCharPosition;

    #[ORM\Column(type: 'float', nullable: true)]
    private ?float $pixelOffset = null;

    #[ORM\ManyToOne(inversedBy: 'chords')]
    #[ORM\JoinColumn(nullable: false)]
    private ?Verse $verse = null;

    #[ORM\ManyToOne(inversedBy: 'lyricChords')]
    #[ORM\JoinColumn(nullable: false)]
    private ?ChordRoot $chord = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getCharPosition(): ?int
    {
        return $this->charPosition;
    }

    public function setCharPosition(int $charPosition): static
    {
        $this->charPosition = $charPosition;

        return $this;
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

    public function getChord(): ?ChordRoot
    {
        return $this->chord;
    }

    public function setChord(?ChordRoot $chord): static
    {
        $this->chord = $chord;

        return $this;
    }
}
