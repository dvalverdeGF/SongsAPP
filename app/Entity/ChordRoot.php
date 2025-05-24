<?php

namespace App\Entity;

use App\Repository\ChordRootRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: ChordRootRepository::class)]
class ChordRoot
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 1)]
    private ?string $name = null;

    #[ORM\Column(length: 2)]
    private ?string $nameLatin = null;

    /**
     * @var Collection<int, LyricChord>
     */
    #[ORM\OneToMany(targetEntity: LyricChord::class, mappedBy: 'chord', orphanRemoval: true)]
    private Collection $lyricChords;

    public function __construct()
    {
        $this->lyricChords = new ArrayCollection();
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

    /**
     * @return Collection<int, LyricChord>
     */
    public function getLyricChords(): Collection
    {
        return $this->lyricChords;
    }

    public function addLyricChord(LyricChord $lyricChord): static
    {
        if (!$this->lyricChords->contains($lyricChord)) {
            $this->lyricChords->add($lyricChord);
            $lyricChord->setChord($this);
        }

        return $this;
    }

    public function removeLyricChord(LyricChord $lyricChord): static
    {
        if ($this->lyricChords->removeElement($lyricChord)) {
            // set the owning side to null (unless already changed)
            if ($lyricChord->getChord() === $this) {
                $lyricChord->setChord(null);
            }
        }

        return $this;
    }
}
