<?php

namespace App\Entity;

use App\Repository\LyricRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: LyricRepository::class)]
class Lyric
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\OneToMany(mappedBy: 'lyric', targetEntity: Verse::class, cascade: ['persist', 'remove'], orphanRemoval: true)]
    private Collection $verses;

    #[ORM\OneToOne(inversedBy: 'lyric')]
    private ?Song $song = null;

    public function __construct()
    {
        $this->verses = new ArrayCollection();
    }
    public function getId(): ?int
    {
        return $this->id;
    }

    /**
     * @return Collection<int, Verse>
     */
    public function getVerses(): Collection
    {
        return $this->verses;
    }

    public function addVerse(Verse $verse): static
    {
        if (!$this->verses->contains($verse)) {
            $this->verses->add($verse);
            $verse->setLyric($this);
        }

        return $this;
    }

    public function removeVerse(Verse $verse): static
    {
        if ($this->verses->removeElement($verse)) {
            // set the owning side to null (unless already changed)
            if ($verse->getLyric() === $this) {
                $verse->setLyric(null);
            }
        }

        return $this;
    }

    public function getSong(): ?Song
    {
        return $this->song;
    }

    public function setSong(?Song $song): static
    {
        $this->song = $song;

        return $this;
    }


}
